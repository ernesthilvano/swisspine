package com.swisspine.service;

import com.swisspine.dto.PerformanceStatisticsDTO;
import com.swisspine.repository.*;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.boot.info.BuildProperties;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {

    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;
    private final MeterRegistry meterRegistry;
    private final PlannerRepository plannerRepository;
    private final ExternalConnectionRepository externalConnectionRepository;
    private final FundRepository fundRepository;
    private final FundAliasRepository fundAliasRepository;
    private final SourceNameRepository sourceNameRepository;
    private final RunNameRepository runNameRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportNameRepository reportNameRepository;

    private final long startTime = System.currentTimeMillis();

    /**
     * Collect and aggregate performance statistics from all sources.
     */
    public PerformanceStatisticsDTO getPerformanceStatistics() {
        log.debug("Collecting performance statistics");

        return PerformanceStatisticsDTO.builder()
                .timestamp(Instant.now())
                .uptime(calculateUptime())
                .database(collectDatabaseStatistics())
                .endpoints(collectEndpointStatistics())
                .jvm(collectJvmStatistics())
                .build();
    }

    private String calculateUptime() {
        long uptimeMs = System.currentTimeMillis() - startTime;
        long hours = uptimeMs / (1000 * 60 * 60);
        long minutes = (uptimeMs / (1000 * 60)) % 60;
        long seconds = (uptimeMs / 1000) % 60;
        return String.format("%dh %dm %ds", hours, minutes, seconds);
    }

    private PerformanceStatisticsDTO.DatabaseStatistics collectDatabaseStatistics() {
        Map<String, Long> totalRecords = new HashMap<>();
        totalRecords.put("planners", plannerRepository.count());
        totalRecords.put("funds", fundRepository.count());
        totalRecords.put("externalConnections", externalConnectionRepository.count());
        totalRecords.put("fundAliases", fundAliasRepository.count());
        totalRecords.put("sourceNames", sourceNameRepository.count());
        totalRecords.put("runNames", runNameRepository.count());
        totalRecords.put("reportTypes", reportTypeRepository.count());
        totalRecords.put("reportNames", reportNameRepository.count());

        return PerformanceStatisticsDTO.DatabaseStatistics.builder()
                .totalRecords(totalRecords)
                .queryStats(collectQueryStatistics())
                .connectionPool(collectConnectionPoolStatistics())
                .build();
    }

    private PerformanceStatisticsDTO.QueryStatistics collectQueryStatistics() {
        try {
            SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
            Statistics stats = sessionFactory.getStatistics();

            long queryCount = stats.getQueryExecutionCount();
            long queryTime = stats.getQueryExecutionMaxTime();
            double avgTime = queryCount > 0 ? (double) queryTime / queryCount : 0.0;

            // Calculate cache hit rate
            long hitCount = stats.getSecondLevelCacheHitCount();
            long missCount = stats.getSecondLevelCacheMissCount();
            double cacheHitRate = (hitCount + missCount) > 0
                    ? (double) hitCount / (hitCount + missCount) * 100
                    : 0.0;

            return PerformanceStatisticsDTO.QueryStatistics.builder()
                    .avgExecutionTime(avgTime)
                    .slowQueries(0) // Will be enhanced later with actual slow query detection
                    .totalQueries(queryCount)
                    .cacheHitRate(cacheHitRate)
                    .build();
        } catch (Exception e) {
            log.warn("Unable to collect query statistics: {}", e.getMessage());
            return PerformanceStatisticsDTO.QueryStatistics.builder()
                    .avgExecutionTime(0.0)
                    .slowQueries(0)
                    .totalQueries(0L)
                    .cacheHitRate(0.0)
                    .build();
        }
    }

    private PerformanceStatisticsDTO.ConnectionPoolStatistics collectConnectionPoolStatistics() {
        try {
            if (dataSource instanceof HikariDataSource hikariDataSource) {
                HikariPoolMXBean poolMXBean = hikariDataSource.getHikariPoolMXBean();
                return PerformanceStatisticsDTO.ConnectionPoolStatistics.builder()
                        .active(poolMXBean.getActiveConnections())
                        .idle(poolMXBean.getIdleConnections())
                        .max(hikariDataSource.getMaximumPoolSize())
                        .waiting(poolMXBean.getThreadsAwaitingConnection())
                        .build();
            }
        } catch (Exception e) {
            log.warn("Unable to collect connection pool statistics: {}", e.getMessage());
        }

        return PerformanceStatisticsDTO.ConnectionPoolStatistics.builder()
                .active(0).idle(0).max(0).waiting(0).build();
    }

    private Map<String, PerformanceStatisticsDTO.EndpointStatistics> collectEndpointStatistics() {
        Map<String, PerformanceStatisticsDTO.EndpointStatistics> endpointStats = new HashMap<>();

        // Collect metrics for our main endpoints with HTTP method classification
        String[] endpoints = {
                "/api/planners",
                "/api/planners/{id}",
                "/api/external-connections",
                "/api/external-connections/{id}",
                "/api/master-data/funds",
                "/api/statistics/performance"
        };

        String[] methods = { "GET", "POST", "PUT", "DELETE" };

        for (String endpoint : endpoints) {
            for (String method : methods) {
                try {
                    Timer timer = meterRegistry.find("http.server.requests")
                            .tag("uri", endpoint)
                            .tag("method", method) // Add method tag for separation
                            .timer();

                    if (timer != null && timer.count() > 0) {
                        double avgTime = timer.mean(java.util.concurrent.TimeUnit.MILLISECONDS);
                        double p95Time = timer.percentile(0.95, java.util.concurrent.TimeUnit.MILLISECONDS);
                        long count = timer.count();

                        // Use "METHOD /path" as the key for better clarity
                        String key = method + " " + endpoint;
                        endpointStats.put(key, PerformanceStatisticsDTO.EndpointStatistics.builder()
                                .avgResponseTime(avgTime)
                                .p95ResponseTime(p95Time)
                                .requestCount(count)
                                .errorRate(0.0) // Will be enhanced with actual error tracking
                                .build());
                    }
                } catch (Exception e) {
                    log.debug("Unable to collect stats for {} {}: {}", method, endpoint, e.getMessage());
                }
            }
        }

        return endpointStats;
    }

    private PerformanceStatisticsDTO.JvmStatistics collectJvmStatistics() {
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        long memoryUsed = memoryMXBean.getHeapMemoryUsage().getUsed() / (1024 * 1024); // MB
        long memoryMax = memoryMXBean.getHeapMemoryUsage().getMax() / (1024 * 1024); // MB

        long gcCount = ManagementFactory.getGarbageCollectorMXBeans().stream()
                .mapToLong(GarbageCollectorMXBean::getCollectionCount)
                .sum();

        return PerformanceStatisticsDTO.JvmStatistics.builder()
                .memoryUsed(memoryUsed)
                .memoryMax(memoryMax)
                .gcCount(gcCount)
                .threadCount(threadMXBean.getThreadCount())
                .build();
    }
}

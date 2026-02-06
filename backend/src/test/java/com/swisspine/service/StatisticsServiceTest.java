package com.swisspine.service;

import com.swisspine.dto.PerformanceStatisticsDTO;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    private EntityManagerFactory entityManagerFactory;

    private javax.sql.DataSource dataSource; // Use javax.sql.DataSource as per service definition

    private MeterRegistry meterRegistry = new io.micrometer.core.instrument.simple.SimpleMeterRegistry();

    @Mock
    private com.swisspine.repository.PlannerRepository plannerRepository;

    @Mock
    private com.swisspine.repository.ExternalConnectionRepository externalConnectionRepository;

    @Mock
    private com.swisspine.repository.FundRepository fundRepository;

    @Mock
    private com.swisspine.repository.FundAliasRepository fundAliasRepository;

    @Mock
    private com.swisspine.repository.SourceNameRepository sourceNameRepository;

    @Mock
    private com.swisspine.repository.RunNameRepository runNameRepository;

    @Mock
    private com.swisspine.repository.ReportTypeRepository reportTypeRepository;

    @Mock
    private com.swisspine.repository.ReportNameRepository reportNameRepository;

    private SessionFactory sessionFactory;

    @Mock
    private Statistics hibernateStats;

    @InjectMocks
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        sessionFactory = (SessionFactory) java.lang.reflect.Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { SessionFactory.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("getStatistics")) {
                        return hibernateStats;
                    }
                    return null;
                });

        // Use JDK Dynamic Proxy for EntityManagerFactory to avoid Mockito inline issues
        // with AutoCloseable on Java 21+
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) java.lang.reflect.Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { EntityManagerFactory.class },
                (proxy, method, args) -> {
                    if (method.getName().equals("unwrap") && args.length > 0 && args[0] == SessionFactory.class) {
                        return sessionFactory;
                    }
                    return null;
                });

        // Use proxy for DataSource as well
        dataSource = (javax.sql.DataSource) java.lang.reflect.Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] { javax.sql.DataSource.class },
                (proxy, method, args) -> null);

        statisticsService = new StatisticsService(
                entityManagerFactory,
                dataSource,
                meterRegistry,
                plannerRepository,
                externalConnectionRepository,
                fundRepository,
                fundAliasRepository,
                sourceNameRepository,
                runNameRepository,
                reportTypeRepository,
                reportNameRepository);

    }

    @Test
    void getPerformanceStatistics_ShouldReturnCompleteDTO() {
        // Arrange
        when(hibernateStats.getQueryExecutionCount()).thenReturn(100L);
        when(hibernateStats.getQueryExecutionMaxTime()).thenReturn(50L);
        when(hibernateStats.getSecondLevelCacheHitCount()).thenReturn(80L);
        when(hibernateStats.getSecondLevelCacheMissCount()).thenReturn(20L);

        // Since we are using SimpleMeterRegistry, metrics are empty by default unless
        // recorded.
        // We just verify that the method runs without error and returns non-null DTO.

        // Act
        PerformanceStatisticsDTO result = statisticsService.getPerformanceStatistics();

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTimestamp());
        assertNotNull(result.getUptime());

        // Database Stats Validation
        assertEquals(100L, result.getDatabase().getQueryStats().getTotalQueries());
        assertEquals(80.0, result.getDatabase().getQueryStats().getCacheHitRate()); // 80 / (80+20) = 0.8 * 100 = 80%

        // Endpoint Stats Validation (should be empty with SimpleMeterRegistry and no
        // recorded metrics)
        assertTrue(result.getEndpoints().isEmpty());
    }

    @Test
    void getPerformanceStatistics_ShouldHandleEmptyMetricsGracefully() {
        // Arrange
        // With SimpleMeterRegistry, find will return null if no meter exists,
        // so we don't need to mock search or timer explicitly for this case.

        // Act
        PerformanceStatisticsDTO result = statisticsService.getPerformanceStatistics();

        // Assert
        assertNotNull(result);
        assertTrue(result.getEndpoints().isEmpty());
    }
}

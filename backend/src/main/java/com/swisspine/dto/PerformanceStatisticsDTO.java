package com.swisspine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * DTO for system performance statistics.
 * Aggregates metrics from database, JVM, and application endpoints.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceStatisticsDTO {

    private Instant timestamp;
    private String uptime;
    private DatabaseStatistics database;
    private Map<String, EndpointStatistics> endpoints;
    private JvmStatistics jvm;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseStatistics {
        private Map<String, Long> totalRecords;
        private QueryStatistics queryStats;
        private ConnectionPoolStatistics connectionPool;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryStatistics {
        private Double avgExecutionTime;
        private Integer slowQueries;
        private Long totalQueries;
        private Double cacheHitRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConnectionPoolStatistics {
        private Integer active;
        private Integer idle;
        private Integer max;
        private Integer waiting;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EndpointStatistics {
        private Double avgResponseTime;
        private Double p95ResponseTime;
        private Long requestCount;
        private Double errorRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JvmStatistics {
        private Long memoryUsed;
        private Long memoryMax;
        private Long gcCount;
        private Integer threadCount;
    }
}

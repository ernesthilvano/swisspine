package com.swisspine.controller;

import com.swisspine.dto.PerformanceStatisticsDTO;
import com.swisspine.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for performance statistics and monitoring.
 * Provides endpoints for retrieving system metrics and performance data.
 */
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Statistics", description = "Performance monitoring and statistics APIs")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/performance")
    @Operation(summary = "Get performance statistics", description = "Retrieve comprehensive performance metrics including database, JVM, and endpoint statistics")
    public ResponseEntity<PerformanceStatisticsDTO> getPerformanceStatistics() {
        log.debug("GET /api/statistics/performance");

        PerformanceStatisticsDTO statistics = statisticsService.getPerformanceStatistics();
        return ResponseEntity.ok(statistics);
    }
}

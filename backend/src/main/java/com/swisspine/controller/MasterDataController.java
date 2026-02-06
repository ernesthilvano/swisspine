package com.swisspine.controller;

import com.swisspine.dto.MasterDataDTO;
import com.swisspine.service.MasterDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Master Data management (dropdowns).
 * 
 * Provides endpoints for managing lookup data used in planners:
 * - Source Names
 * - Run Names
 * - Report Types
 * - Report Names
 * - Funds
 * 
 * All lists are returned sorted A-Z as per requirements.
 * 
 * @author SwissPine Engineering Team
 */
@RestController
@RequestMapping("/api/master-data")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Master Data", description = "Dropdown data management APIs")
public class MasterDataController {

    private final MasterDataService service;

    // ==================== Source Names ====================

    @GetMapping("/source-names")
    @Operation(summary = "Get all source names (sorted A-Z)")
    public ResponseEntity<List<MasterDataDTO>> getAllSourceNames() {
        return ResponseEntity.ok(service.getAllSourceNames());
    }

    @PostMapping("/source-names")
    @Operation(summary = "Create new source name")
    public ResponseEntity<MasterDataDTO> createSourceName(@Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createSourceName(dto));
    }

    @DeleteMapping("/source-names/{id}")
    @Operation(summary = "Delete source name")
    public ResponseEntity<Void> deleteSourceName(@PathVariable Long id) {
        service.deleteSourceName(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Run Names ====================

    @GetMapping("/run-names")
    @Operation(summary = "Get all run names (sorted A-Z)")
    public ResponseEntity<List<MasterDataDTO>> getAllRunNames() {
        return ResponseEntity.ok(service.getAllRunNames());
    }

    @PostMapping("/run-names")
    @Operation(summary = "Create new run name")
    public ResponseEntity<MasterDataDTO> createRunName(@Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createRunName(dto));
    }

    @DeleteMapping("/run-names/{id}")
    @Operation(summary = "Delete run name")
    public ResponseEntity<Void> deleteRunName(@PathVariable Long id) {
        service.deleteRunName(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Report Types ====================

    @GetMapping("/report-types")
    @Operation(summary = "Get all report types (sorted A-Z)")
    public ResponseEntity<List<MasterDataDTO>> getAllReportTypes() {
        return ResponseEntity.ok(service.getAllReportTypes());
    }

    @PostMapping("/report-types")
    @Operation(summary = "Create new report type")
    public ResponseEntity<MasterDataDTO> createReportType(@Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReportType(dto));
    }

    @DeleteMapping("/report-types/{id}")
    @Operation(summary = "Delete report type")
    public ResponseEntity<Void> deleteReportType(@PathVariable Long id) {
        service.deleteReportType(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Report Names ====================

    @GetMapping("/report-names")
    @Operation(summary = "Get all report names (sorted A-Z)")
    public ResponseEntity<List<MasterDataDTO>> getAllReportNames() {
        return ResponseEntity.ok(service.getAllReportNames());
    }

    @GetMapping("/report-names/by-type/{typeId}")
    @Operation(summary = "Get report names by type ID")
    public ResponseEntity<List<MasterDataDTO>> getReportNamesByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(service.getReportNamesByType(typeId));
    }

    @PostMapping("/report-names")
    @Operation(summary = "Create new report name")
    public ResponseEntity<MasterDataDTO> createReportName(@Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createReportName(dto));
    }

    @DeleteMapping("/report-names/{id}")
    @Operation(summary = "Delete report name")
    public ResponseEntity<Void> deleteReportName(@PathVariable Long id) {
        service.deleteReportName(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== Funds ====================

    @GetMapping("/funds")
    @Operation(summary = "Get all funds (sorted A-Z)")
    public ResponseEntity<List<MasterDataDTO>> getAllFunds() {
        return ResponseEntity.ok(service.getAllFunds());
    }

    @PostMapping("/funds")
    @Operation(summary = "Create new fund")
    public ResponseEntity<MasterDataDTO> createFund(@Valid @RequestBody MasterDataDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createFund(dto));
    }

    @DeleteMapping("/funds/{id}")
    @Operation(summary = "Delete fund")
    public ResponseEntity<Void> deleteFund(@PathVariable Long id) {
        service.deleteFund(id);
        return ResponseEntity.noContent().build();
    }
}

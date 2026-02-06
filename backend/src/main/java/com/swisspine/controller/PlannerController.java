package com.swisspine.controller;

import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.dto.PlannerDTO;
import com.swisspine.service.PlannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Planner management.
 * 
 * Provides endpoints for CRUD operations, filtering, and pagination.
 * All responses follow consistent REST patterns with proper HTTP status codes.
 * 
 * @author SwissPine Engineering Team
 */
@RestController
@RequestMapping("/api/planners")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Planners", description = "Planner management APIs")
public class PlannerController {

    private final PlannerService service;

    @GetMapping
    @Operation(summary = "Get all planners", description = "Retrieve paginated list of planners with optional status filter")
    public ResponseEntity<PageableResponseDTO<PlannerDTO>> getAll(
            @Parameter(description = "Filter by status (Draft, In Progress, Finished)") @RequestParam(required = false) String status,

            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        log.debug("GET /api/planners - status: {}, page: {}, size: {}", status, page, size);

        PageableResponseDTO<PlannerDTO> response = service.findAll(status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search planners by name", description = "Search planners using case-insensitive partial name matching with optional status filter")
    public ResponseEntity<PageableResponseDTO<PlannerDTO>> search(
            @Parameter(description = "Search query for planner name") @RequestParam String q,

            @Parameter(description = "Filter by status (optional)") @RequestParam(required = false) String status,

            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        log.debug("GET /api/planners/search - query: {}, status: {}, page: {}, size: {}", q, status, page, size);

        PageableResponseDTO<PlannerDTO> response = service.search(q, status, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get planner by ID")
    public ResponseEntity<PlannerDTO> getById(
            @Parameter(description = "Planner ID") @PathVariable Long id) {

        log.debug("GET /api/planners/{}", id);

        PlannerDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Create new planner")
    public ResponseEntity<PlannerDTO> create(
            @Valid @RequestBody PlannerDTO dto) {

        log.info("POST /api/planners - name: {}", dto.getName());

        PlannerDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing planner")
    public ResponseEntity<PlannerDTO> update(
            @Parameter(description = "Planner ID") @PathVariable Long id,

            @Valid @RequestBody PlannerDTO dto) {

        log.info("PUT /api/planners/{} - name: {}", id, dto.getName());

        PlannerDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete planner")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Planner ID") @PathVariable Long id) {

        log.info("DELETE /api/planners/{}", id);

        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.swisspine.controller;

import com.swisspine.dto.ExternalConnectionDTO;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.service.ExternalConnectionService;
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
 * REST controller for External Connection management.
 * 
 * Provides endpoints for CRUD operations, search, and pagination.
 * All responses follow consistent REST patterns with proper HTTP status codes.
 * 
 * @author SwissPine Engineering Team
 */
@RestController
@RequestMapping("/api/external-connections")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "External Connections", description = "External system connection management APIs")
public class ExternalConnectionController {

    private final ExternalConnectionService service;

    @GetMapping
    @Operation(summary = "Get all external connections", description = "Retrieve paginated list of external connections with optional search")
    public ResponseEntity<PageableResponseDTO<ExternalConnectionDTO>> getAll(
            @Parameter(description = "Search term for connection name") @RequestParam(required = false) String search,

            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Page size (5, 10, or 25)") @RequestParam(defaultValue = "10") int size) {

        log.debug("GET /api/external-connections - search: {}, page: {}, size: {}", search, page, size);

        PageableResponseDTO<ExternalConnectionDTO> response = service.findAll(search, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get external connection by ID")
    public ResponseEntity<ExternalConnectionDTO> getById(
            @Parameter(description = "Connection ID") @PathVariable Long id) {

        log.debug("GET /api/external-connections/{}", id);

        ExternalConnectionDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Create new external connection")
    public ResponseEntity<ExternalConnectionDTO> create(
            @Valid @RequestBody ExternalConnectionDTO dto) {

        log.info("POST /api/external-connections - name: {}", dto.getName());

        ExternalConnectionDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing external connection")
    public ResponseEntity<ExternalConnectionDTO> update(
            @Parameter(description = "Connection ID") @PathVariable Long id,

            @Valid @RequestBody ExternalConnectionDTO dto) {

        log.info("PUT /api/external-connections/{} - name: {}", id, dto.getName());

        ExternalConnectionDTO updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete external connection")
    public ResponseEntity<Void> delete(
            @Parameter(description = "Connection ID") @PathVariable Long id) {

        log.info("DELETE /api/external-connections/{}", id);

        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/copy")
    @Operation(summary = "Copy existing external connection")
    public ResponseEntity<ExternalConnectionDTO> copy(
            @Parameter(description = "Connection ID to copy") @PathVariable Long id) {

        log.info("POST /api/external-connections/{}/copy", id);

        ExternalConnectionDTO copied = service.copy(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(copied);
    }
}

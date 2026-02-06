package com.swisspine.service;

import com.swisspine.dto.ExternalConnectionDTO;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.dto.PlannerDTO;
import com.swisspine.entity.ExternalConnection;
import com.swisspine.entity.Planner;
import com.swisspine.exception.ResourceNotFoundException;
import com.swisspine.repository.ExternalConnectionRepository;
import com.swisspine.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for Planner business logic.
 * 
 * Provides CRUD operations with pagination and filtering by status.
 * 
 * @author SwissPine Engineering Team
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PlannerService {

    private final PlannerRepository repository;
    private final ExternalConnectionRepository connectionRepository;

    /**
     * Find all planners with pagination and optional status filtering.
     */
    @Transactional(readOnly = true)
    public PageableResponseDTO<PlannerDTO> findAll(String status, int page, int size) {
        log.debug("Finding planners - status: {}, page: {}, size: {}", status, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Planner> entityPage;

        if (status != null && !status.trim().isEmpty()) {
            entityPage = repository.findByStatusOrderByCreatedAtDesc(status.trim(), pageable);
        } else {
            entityPage = repository.findAll(pageable);
        }

        Page<PlannerDTO> dtoPage = entityPage.map(this::toDTO);

        return PageableResponseDTO.from(dtoPage);
    }

    /**
     * Search planners by name with pagination.
     * Uses case-insensitive partial matching.
     * Optionally filters by status.
     */
    @Transactional(readOnly = true)
    public PageableResponseDTO<PlannerDTO> search(String query, String status, int page, int size) {
        log.debug("Searching planners - query: {}, status: {}, page: {}, size: {}", query, status, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Planner> entityPage;

        if (status != null && !status.trim().isEmpty()) {
            entityPage = repository.searchByNameAndStatus(query, status.trim(), pageable);
        } else {
            entityPage = repository.searchByName(query, pageable);
        }

        Page<PlannerDTO> dtoPage = entityPage.map(this::toDTO);

        return PageableResponseDTO.from(dtoPage);
    }

    /**
     * Find planner by ID.
     */
    @Transactional(readOnly = true)
    public PlannerDTO findById(Long id) {
        log.debug("Finding planner by ID: {}", id);

        Planner entity = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Planner", id));

        return toDTO(entity);
    }

    /**
     * Create a new planner.
     */
    public PlannerDTO create(PlannerDTO dto) {
        log.info("Creating planner: {}", dto.getName());

        Planner entity = toEntity(dto);
        Planner saved = repository.save(entity);

        log.info("Created planner with ID: {}", saved.getId());

        return toDTO(saved);
    }

    /**
     * Update an existing planner.
     */
    public PlannerDTO update(Long id, PlannerDTO dto) {
        log.info("Updating planner ID: {}", id);

        Planner existing = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("Planner", id));

        // Update fields
        updateEntityFromDTO(existing, dto);
        Planner saved = repository.save(existing);

        log.info("Successfully updated planner ID: {}", id);

        return toDTO(saved);
    }

    /**
     * Delete a planner by ID.
     */
    public void delete(Long id) {
        log.info("Deleting planner ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Planner with ID " + id + " not found");
        }

        repository.deleteById(id);
        log.info("Successfully deleted planner ID: {}", id);
    }

    // ==================== Private Helper Methods ====================

    private PlannerDTO toDTO(Planner entity) {
        PlannerDTO.PlannerDTOBuilder builder = PlannerDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .plannerType(entity.getPlannerType())
                .status(entity.getStatus())
                .finishedAt(entity.getFinishedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .version(entity.getVersion());

        // Map external system config if present
        if (entity.getExternalSystemConfig() != null) {
            builder.externalSystemConfigId(entity.getExternalSystemConfig().getId());
            builder.externalSystemConfig(toConnectionDTO(entity.getExternalSystemConfig()));
        }

        return builder.build();
    }

    private Planner toEntity(PlannerDTO dto) {
        Planner.PlannerBuilder builder = Planner.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .plannerType(dto.getPlannerType())
                .status(dto.getStatus() != null ? dto.getStatus() : "Draft");

        // Link external connection if provided
        if (dto.getExternalSystemConfigId() != null) {
            ExternalConnection connection = connectionRepository.findById(dto.getExternalSystemConfigId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "External connection with ID " + dto.getExternalSystemConfigId() + " not found"));
            builder.externalSystemConfig(connection);
        }

        return builder.build();
    }

    private void updateEntityFromDTO(Planner entity, PlannerDTO dto) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPlannerType(dto.getPlannerType());
        entity.setStatus(dto.getStatus());

        // Update external connection if changed
        if (dto.getExternalSystemConfigId() != null) {
            ExternalConnection connection = connectionRepository.findById(dto.getExternalSystemConfigId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "External connection with ID " + dto.getExternalSystemConfigId() + " not found"));
            entity.setExternalSystemConfig(connection);
        } else {
            entity.setExternalSystemConfig(null);
        }
    }

    private ExternalConnectionDTO toConnectionDTO(ExternalConnection entity) {
        return ExternalConnectionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .baseUrl(entity.getBaseUrl())
                .authenticationMethod(entity.getAuthenticationMethod())
                .build();
    }
}

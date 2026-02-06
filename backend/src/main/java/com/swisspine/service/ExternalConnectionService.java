package com.swisspine.service;

import com.swisspine.dto.ExternalConnectionDTO;
import com.swisspine.dto.PageableResponseDTO;
import com.swisspine.entity.ExternalConnection;
import com.swisspine.exception.BusinessRuleViolationException;
import com.swisspine.exception.ResourceNotFoundException;
import com.swisspine.repository.ExternalConnectionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service layer for ExternalConnection business logic.
 * 
 * Implements comprehensive business rules:
 * - Value field immutability after initial set
 * - Default connection uniqueness
 * - Name uniqueness validation
 * - Value field masking for security
 * 
 * @author SwissPine Engineering Team
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ExternalConnectionService {

    private final ExternalConnectionRepository repository;

    /**
     * Find all external connections with pagination and optional search.
     * Masks value fields in response for security.
     */
    @Transactional(readOnly = true)
    public PageableResponseDTO<ExternalConnectionDTO> findAll(String search, int page, int size) {
        log.debug("Finding external connections - search: {}, page: {}, size: {}", search, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<ExternalConnection> entityPage;

        if (search != null && !search.trim().isEmpty()) {
            entityPage = repository.searchByName(search.trim(), pageable);
        } else {
            entityPage = repository.findAll(pageable);
        }

        Page<ExternalConnectionDTO> dtoPage = entityPage.map(this::toDTO);

        // Mask sensitive value fields before returning
        dtoPage.getContent().forEach(ExternalConnectionDTO::maskValueField);

        return PageableResponseDTO.from(dtoPage);
    }

    /**
     * Find external connection by ID.
     * Masks value field in response.
     */
    @Transactional(readOnly = true)
    public ExternalConnectionDTO findById(Long id) {
        log.debug("Finding external connection by ID: {}", id);

        ExternalConnection entity = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("ExternalConnection", id));

        ExternalConnectionDTO dto = toDTO(entity);
        dto.maskValueField();

        return dto;
    }

    /**
     * Create a new external connection.
     * Validates uniqueness and default constraints.
     */
    public ExternalConnectionDTO create(ExternalConnectionDTO dto) {
        log.info("Creating external connection: {}", dto.getName());

        // Validate name uniqueness
        repository.findByNameIgnoreCase(dto.getName()).ifPresent(existing -> {
            throw new BusinessRuleViolationException(
                    "External connection with name '" + dto.getName() + "' already exists");
        });

        // If setting as default, remove default from existing connections
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            repository.findByIsDefaultTrue().ifPresent(existing -> {
                log.debug("Removing default flag from connection: {}", existing.getName());
                existing.setIsDefault(false);
                repository.save(existing);
            });
        }

        ExternalConnection entity = toEntity(dto);
        ExternalConnection saved = repository.save(entity);

        log.info("Created external connection with ID: {}", saved.getId());

        ExternalConnectionDTO result = toDTO(saved);
        result.maskValueField();
        return result;
    }

    /**
     * Update an existing external connection.
     * Enforces value field immutability if already set.
     */
    public ExternalConnectionDTO update(Long id, ExternalConnectionDTO dto) {
        log.info("Updating external connection ID: {}", id);

        ExternalConnection existing = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("ExternalConnection", id));

        // Validate name uniqueness (excluding current entity)
        if (!existing.getName().equalsIgnoreCase(dto.getName())) {
            long count = repository.countByNameAndIdNot(dto.getName(), id);
            if (count > 0) {
                throw new BusinessRuleViolationException(
                        "External connection with name '" + dto.getName() + "' already exists");
            }
        }

        // Enforce value field immutability
        if (Boolean.TRUE.equals(existing.getValueFieldSet()) &&
                dto.getValueField() != null &&
                !dto.getValueField().equals("********")) {

            throw new BusinessRuleViolationException(
                    "Value field cannot be modified once set. Please delete and recreate if needed.");
        }

        // If setting as default, remove default from other connections
        if (Boolean.TRUE.equals(dto.getIsDefault()) && !existing.getIsDefault()) {
            repository.findByIsDefaultTrue().ifPresent(defaultConn -> {
                if (!defaultConn.getId().equals(id)) {
                    log.debug("Removing default flag from connection: {}", defaultConn.getName());
                    defaultConn.setIsDefault(false);
                    repository.save(defaultConn);
                }
            });
        }

        // Update fields
        updateEntityFromDTO(existing, dto);
        ExternalConnection saved = repository.save(existing);

        log.info("Successfully updated external connection ID: {}", id);

        ExternalConnectionDTO result = toDTO(saved);
        result.maskValueField();
        return result;
    }

    /**
     * Delete an external connection by ID.
     * Validates that no planners are using this connection.
     */
    public void delete(Long id) {
        log.info("Deleting external connection ID: {}", id);

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("External connection with ID " + id + " not found");
        }

        // TODO: Add validation to check if any planners reference this connection
        // For now, database constraint (ON DELETE SET NULL) will handle it

        repository.deleteById(id);
        log.info("Successfully deleted external connection ID: {}", id);
    }

    /**
     * Copy an existing external connection.
     * Creates a new connection with " (Copy)" appended to the name.
     * Value field is not copied for security reasons.
     */
    public ExternalConnectionDTO copy(Long id) {
        log.info("Copying external connection ID: {}", id);

        ExternalConnection source = repository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.forId("ExternalConnection", id));

        ExternalConnection copy = ExternalConnection.builder()
                .name(source.getName() + " (Copy)")
                .baseUrl(source.getBaseUrl())
                .authenticationMethod(source.getAuthenticationMethod())
                .keyField(source.getKeyField())
                .authenticationPlace(source.getAuthenticationPlace())
                .isDefault(false) // Copy is never default
                .valueField(null) // Don't copy sensitive value field
                .valueFieldSet(false)
                .build();

        ExternalConnection saved = repository.save(copy);
        log.info("Created copy with ID: {}", saved.getId());

        return toDTO(saved);
    }

    // ==================== Private Helper Methods ====================

    private ExternalConnectionDTO toDTO(ExternalConnection entity) {
        return ExternalConnectionDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .baseUrl(entity.getBaseUrl())
                .authenticationMethod(entity.getAuthenticationMethod())
                .keyField(entity.getKeyField())
                .valueField(entity.getValueField())
                .valueFieldSet(entity.getValueFieldSet())
                .authenticationPlace(entity.getAuthenticationPlace())
                .isDefault(entity.getIsDefault())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .version(entity.getVersion())
                .build();
    }

    private ExternalConnection toEntity(ExternalConnectionDTO dto) {
        return ExternalConnection.builder()
                .name(dto.getName())
                .baseUrl(dto.getBaseUrl())
                .authenticationMethod(dto.getAuthenticationMethod())
                .keyField(dto.getKeyField())
                .valueField(dto.getValueField())
                .authenticationPlace(dto.getAuthenticationPlace())
                .isDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false)
                .build();
    }

    private void updateEntityFromDTO(ExternalConnection entity, ExternalConnectionDTO dto) {
        entity.setName(dto.getName());
        entity.setBaseUrl(dto.getBaseUrl());
        entity.setAuthenticationMethod(dto.getAuthenticationMethod());
        entity.setKeyField(dto.getKeyField());
        entity.setAuthenticationPlace(dto.getAuthenticationPlace());
        entity.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : false);

        // Only update value field if not already set
        if (!Boolean.TRUE.equals(entity.getValueFieldSet()) && dto.getValueField() != null) {
            entity.setValueField(dto.getValueField());
        }
    }
}

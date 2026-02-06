package com.swisspine.service;

import com.swisspine.dto.MasterDataDTO;
import com.swisspine.entity.*;
import com.swisspine.exception.BusinessRuleViolationException;
import com.swisspine.exception.ResourceNotFoundException;
import com.swisspine.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generic service for all master data entities.
 * 
 * Provides CRUD operations for lookup tables:
 * - SourceName
 * - RunName
 * - ReportType
 * - ReportName
 * - Fund
 * 
 * @author SwissPine Engineering Team
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MasterDataService {

    private final SourceNameRepository sourceNameRepository;
    private final RunNameRepository runNameRepository;
    private final ReportTypeRepository reportTypeRepository;
    private final ReportNameRepository reportNameRepository;
    private final FundRepository fundRepository;

    // ==================== SourceName ====================

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllSourceNames() {
        return sourceNameRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MasterDataDTO createSourceName(MasterDataDTO dto) {
        log.info("Creating source name: {}", dto.getName());
        validateUniqueness(sourceNameRepository, dto.getName(), "SourceName");

        SourceName entity = new SourceName();
        entity.setName(dto.getName());
        SourceName saved = sourceNameRepository.save(entity);

        return toDTO(saved);
    }

    public void deleteSourceName(Long id) {
        log.info("Deleting source name ID: {}", id);
        sourceNameRepository.deleteById(id);
    }

    // ==================== RunName ====================

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllRunNames() {
        return runNameRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MasterDataDTO createRunName(MasterDataDTO dto) {
        log.info("Creating run name: {}", dto.getName());
        validateUniqueness(runNameRepository, dto.getName(), "RunName");

        RunName entity = new RunName();
        entity.setName(dto.getName());
        RunName saved = runNameRepository.save(entity);

        return toDTO(saved);
    }

    public void deleteRunName(Long id) {
        log.info("Deleting run name ID: {}", id);
        runNameRepository.deleteById(id);
    }

    // ==================== ReportType ====================

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllReportTypes() {
        return reportTypeRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MasterDataDTO createReportType(MasterDataDTO dto) {
        log.info("Creating report type: {}", dto.getName());
        validateUniqueness(reportTypeRepository, dto.getName(), "ReportType");

        ReportType entity = new ReportType();
        entity.setName(dto.getName());
        ReportType saved = reportTypeRepository.save(entity);

        return toDTO(saved);
    }

    public void deleteReportType(Long id) {
        log.info("Deleting report type ID: {}", id);
        reportTypeRepository.deleteById(id);
    }

    // ==================== ReportName ====================

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllReportNames() {
        return reportNameRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getReportNamesByType(Long typeId) {
        return reportNameRepository.findByReportTypeIdOrderByNameAsc(typeId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MasterDataDTO createReportName(MasterDataDTO dto) {
        log.info("Creating report name: {}", dto.getName());
        validateUniqueness(reportNameRepository, dto.getName(), "ReportName");

        ReportName entity = new ReportName();
        entity.setName(dto.getName());
        ReportName saved = reportNameRepository.save(entity);

        return toDTO(saved);
    }

    public void deleteReportName(Long id) {
        log.info("Deleting report name ID: {}", id);
        reportNameRepository.deleteById(id);
    }

    // ==================== Fund ====================

    @Transactional(readOnly = true)
    public List<MasterDataDTO> getAllFunds() {
        return fundRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MasterDataDTO createFund(MasterDataDTO dto) {
        log.info("Creating fund: {}", dto.getName());
        validateUniqueness(fundRepository, dto.getName(), "Fund");

        Fund entity = new Fund();
        entity.setName(dto.getName());
        Fund saved = fundRepository.save(entity);

        return toDTO(saved);
    }

    public void deleteFund(Long id) {
        log.info("Deleting fund ID: {}", id);
        fundRepository.deleteById(id);
    }

    // ==================== Helper Methods ====================

    private void validateUniqueness(JpaRepository<?, Long> repository, String name, String entityType) {
        // Generic uniqueness validation - relies on unique constraints in DB as
        // fallback
        // For production, could add specific checks per repository
    }

    private MasterDataDTO toDTO(Object entity) {
        if (entity instanceof SourceName e) {
            return MasterDataDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build();
        } else if (entity instanceof RunName e) {
            return MasterDataDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build();
        } else if (entity instanceof ReportType e) {
            return MasterDataDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build();
        } else if (entity instanceof ReportName e) {
            return MasterDataDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build();
        } else if (entity instanceof Fund e) {
            return MasterDataDTO.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .createdAt(e.getCreatedAt())
                    .updatedAt(e.getUpdatedAt())
                    .build();
        }
        throw new IllegalArgumentException("Unsupported entity type");
    }
}

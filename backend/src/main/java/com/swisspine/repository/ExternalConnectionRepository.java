package com.swisspine.repository;

import com.swisspine.entity.ExternalConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for ExternalConnection entity operations.
 * 
 * Provides CRUD operations and custom queries for search and pagination.
 * Spring Data JPA automatically implements these methods at runtime.
 * 
 * @author SwissPine Engineering Team
 */
@Repository
public interface ExternalConnectionRepository extends JpaRepository<ExternalConnection, Long> {

    /**
     * Find external connection by name (case-insensitive search).
     * Used for duplicate validation during create/update.
     */
    Optional<ExternalConnection> findByNameIgnoreCase(String name);

    /**
     * Find the default external connection.
     * Only one connection should be default (enforced by DB constraint).
     */
    Optional<ExternalConnection> findByIsDefaultTrue();

    /**
     * Search external connections by name with pagination.
     * Uses case-insensitive LIKE query for flexible search.
     * 
     * @param searchTerm The search term to match against name (supports partial
     *                   matches)
     * @param pageable   Pagination parameters (page number, size, sorting)
     * @return Page of matching external connections
     */
    @Query("SELECT ec FROM ExternalConnection ec WHERE " +
            "LOWER(ec.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ExternalConnection> searchByName(
            @Param("searchTerm") String searchTerm,
            Pageable pageable);

    /**
     * Count connections with the same name (excluding a specific ID).
     * Used for duplicate validation during updates.
     */
    @Query("SELECT COUNT(ec) FROM ExternalConnection ec WHERE " +
            "LOWER(ec.name) = LOWER(:name) AND ec.id <> :excludeId")
    long countByNameAndIdNot(@Param("name") String name, @Param("excludeId") Long excludeId);

    /**
     * Check if another connection is already set as default.
     * Used before changing the default flag.
     */
    @Query("SELECT COUNT(ec) FROM ExternalConnection ec WHERE " +
            "ec.isDefault = true AND ec.id <> :excludeId")
    long countDefaultConnectionsExcluding(@Param("excludeId") Long excludeId);
}

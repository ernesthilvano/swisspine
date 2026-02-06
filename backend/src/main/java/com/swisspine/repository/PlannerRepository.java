package com.swisspine.repository;

import com.swisspine.entity.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Planner entity operations.
 * 
 * Includes performance-optimized queries using @EntityGraph to prevent N+1
 * query problems.
 * 
 * @author SwissPine Engineering Team
 */
@Repository
public interface PlannerRepository extends JpaRepository<Planner, Long> {

        /**
         * Find planner by ID with all relationships eagerly loaded.
         * Uses @EntityGraph to fetch funds and sources in a single query.
         * 
         * This prevents N+1 query problems when accessing related entities.
         */
        @EntityGraph(attributePaths = { "funds", "funds.fund", "funds.fundAlias", "sources", "externalSystemConfig" })
        Optional<Planner> findWithRelationsById(Long id);

        /**
         * Search planners by name with pagination.
         * Case-insensitive search supporting partial matches.
         * 
         * @param searchTerm The search term to match against name
         * @param pageable   Pagination parameters
         * @return Page of matching planners
         */
        @Query("SELECT p FROM Planner p WHERE " +
                        "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
        Page<Planner> searchByName(
                        @Param("searchTerm") String searchTerm,
                        Pageable pageable);

        /**
         * Search planners by name and status with pagination.
         */
        @Query("SELECT p FROM Planner p WHERE " +
                        "LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
                        "AND p.status = :status")
        Page<Planner> searchByNameAndStatus(
                        @Param("searchTerm") String searchTerm,
                        @Param("status") String status,
                        Pageable pageable);

        /**
         * Find planners by status with pagination.
         * Enables filtering by workflow state (Draft, Running, Finished, Failed).
         */
        Page<Planner> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);
}

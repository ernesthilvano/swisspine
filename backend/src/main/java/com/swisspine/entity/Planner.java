package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a data planner configuration.
 * 
 * A planner orchestrates data processing workflows with associated sources,
 * runs, reports, and funds. It references an external connection for
 * integration
 * with external systems.
 * 
 * Business Rules:
 * - Planners can have multiple funds, sources, runs, and reports
 * - Status tracks the lifecycle (Draft, Running, Finished, Failed)
 * - finished_at timestamp is set when status changes to Finished
 * - Cascade delete applied to all child relationships
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "planners", indexes = {
        @Index(name = "idx_planner_name", columnList = "name"),
        @Index(name = "idx_planner_status", columnList = "status"),
        @Index(name = "idx_planner_created", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "funds", "sources" }) // Prevent circular references in toString
public class Planner extends BaseEntity {

    @NotBlank(message = "Planner name is required")
    @Size(max = 255, message = "Planner name cannot exceed 255 characters")
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Size(max = 100, message = "Planner type cannot exceed 100 characters")
    @Column(name = "planner_type", length = 100)
    private String plannerType;

    /**
     * Reference to the external system connection configuration.
     * Uses FetchType.LAZY for performance (loaded on demand).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "external_system_config_id", foreignKey = @ForeignKey(name = "fk_planner_ext_conn"))
    private ExternalConnection externalSystemConfig;

    @Size(max = 50, message = "Status cannot exceed 50 characters")
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private String status = "Draft";

    @Column(name = "finished_at")
    private Instant finishedAt;

    /**
     * Associated funds for this planner.
     * Cascade ALL operations to maintain referential integrity.
     * Orphan removal ensures deleted PlannerFund entities are removed from
     * database.
     */
    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PlannerFund> funds = new ArrayList<>();

    /**
     * Associated data sources for this planner.
     * Each source can have multiple runs and reports.
     */
    @OneToMany(mappedBy = "planner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<PlannerSource> sources = new ArrayList<>();

    /**
     * Helper method to add a fund while maintaining bidirectional relationship.
     */
    public void addFund(PlannerFund fund) {
        funds.add(fund);
        fund.setPlanner(this);
    }

    /**
     * Helper method to remove a fund while maintaining bidirectional relationship.
     */
    public void removeFund(PlannerFund fund) {
        funds.remove(fund);
        fund.setPlanner(null);
    }

    /**
     * Helper method to add a source while maintaining bidirectional relationship.
     */
    public void addSource(PlannerSource source) {
        sources.add(source);
        source.setPlanner(this);
    }

    /**
     * Helper method to remove a source while maintaining bidirectional
     * relationship.
     */
    public void removeSource(PlannerSource source) {
        sources.remove(source);
        source.setPlanner(null);
    }

    /**
     * Pre-update hook to set finished_at timestamp when status changes to Finished.
     */
    @PreUpdate
    private void onUpdate() {
        if ("Finished".equalsIgnoreCase(status) && finishedAt == null) {
            finishedAt = Instant.now();
        }
    }
}

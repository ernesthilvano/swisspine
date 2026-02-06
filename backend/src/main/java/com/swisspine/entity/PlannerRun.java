package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a run configuration for a planner source.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "planner_runs", indexes = {
        @Index(name = "idx_planner_runs_source", columnList = "planner_source_id"),
        @Index(name = "idx_planner_runs_order", columnList = "planner_source_id, display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "plannerSource")
public class PlannerRun extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planner_source_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planner_run_source"))
    private PlannerSource plannerSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "run_name_id", foreignKey = @ForeignKey(name = "fk_planner_run_name"))
    private RunName runName;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}

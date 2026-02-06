package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity representing a report configuration for a planner source.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "planner_reports", indexes = {
        @Index(name = "idx_planner_reports_source", columnList = "planner_source_id"),
        @Index(name = "idx_planner_reports_order", columnList = "planner_source_id, display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "plannerSource")
public class PlannerReport extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planner_source_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planner_report_source"))
    private PlannerSource plannerSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_type_id", foreignKey = @ForeignKey(name = "fk_planner_report_type"))
    private ReportType reportType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_name_id", foreignKey = @ForeignKey(name = "fk_planner_report_name"))
    private ReportName reportName;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;
}

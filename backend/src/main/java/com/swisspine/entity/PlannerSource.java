package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a data source associated with a planner.
 * 
 * Each source can have multiple runs and reports. The display_order field
 * controls the UI ordering of sources within a planner.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "planner_sources", indexes = {
        @Index(name = "idx_planner_sources_planner", columnList = "planner_id"),
        @Index(name = "idx_planner_sources_order", columnList = "planner_id, display_order")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "planner", "runs", "reports" })
public class PlannerSource extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planner_source_planner"))
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_name_id", foreignKey = @ForeignKey(name = "fk_planner_source_name"))
    private SourceName sourceName;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @OneToMany(mappedBy = "plannerSource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<PlannerRun> runs = new ArrayList<>();

    @OneToMany(mappedBy = "plannerSource", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<PlannerReport> reports = new ArrayList<>();

    public void addRun(PlannerRun run) {
        runs.add(run);
        run.setPlannerSource(this);
    }

    public void removeRun(PlannerRun run) {
        runs.remove(run);
        run.setPlannerSource(null);
    }

    public void addReport(PlannerReport report) {
        reports.add(report);
        report.setPlannerSource(this);
    }

    public void removeReport(PlannerReport report) {
        reports.remove(report);
        report.setPlannerSource(null);
    }
}

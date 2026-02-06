package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Junction entity representing the many-to-many relationship between planners
 * and funds.
 * 
 * A planner can have multiple funds, and each fund association can optionally
 * include a fund alias for display purposes.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "planner_funds", indexes = {
        @Index(name = "idx_planner_funds_planner", columnList = "planner_id"),
        @Index(name = "idx_planner_funds_fund", columnList = "fund_id")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_planner_fund", columnNames = { "planner_id", "fund_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = { "planner", "fund" }, callSuper = false)
public class PlannerFund extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "planner_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planner_fund_planner"))
    private Planner planner;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false, foreignKey = @ForeignKey(name = "fk_planner_fund_fund"))
    private Fund fund;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fund_alias_id", foreignKey = @ForeignKey(name = "fk_planner_fund_alias"))
    private FundAlias fundAlias;
}

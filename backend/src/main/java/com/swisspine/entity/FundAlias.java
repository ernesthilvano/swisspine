package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity representing an alias for a fund.
 * A fund can have multiple aliases for different contexts or markets.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "fund_aliases", indexes = {
        @Index(name = "idx_fund_aliases_fund", columnList = "fund_id"),
        @Index(name = "idx_fund_aliases_name", columnList = "alias_name")
}, uniqueConstraints = @UniqueConstraint(name = "uk_fund_alias", columnNames = { "fund_id", "alias_name" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "fund")
public class FundAlias extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fund_id", nullable = false, foreignKey = @ForeignKey(name = "fk_fund_alias_fund"))
    private Fund fund;

    @NotBlank(message = "Alias name is required")
    @Size(max = 255)
    @Column(name = "alias_name", nullable = false)
    private String aliasName;
}

package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Master data entity for funds.
 * A fund can have zero to many aliases.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "funds", indexes = @Index(name = "idx_funds_name", columnList = "name"), uniqueConstraints = @UniqueConstraint(name = "uk_fund_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "aliases")
public class Fund extends BaseEntity {

    @NotBlank(message = "Fund name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "fund", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<FundAlias> aliases = new ArrayList<>();

    public void addAlias(FundAlias alias) {
        aliases.add(alias);
        alias.setFund(this);
    }

    public void removeAlias(FundAlias alias) {
        aliases.remove(alias);
        alias.setFund(null);
    }
}

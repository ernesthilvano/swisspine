package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Master data entity for report names.
 * Report names can be associated with report types.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "report_names", indexes = {
        @Index(name = "idx_report_names_name", columnList = "name"),
        @Index(name = "idx_report_names_type", columnList = "report_type_id")
}, uniqueConstraints = @UniqueConstraint(name = "uk_report_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportName extends BaseEntity {

    @NotBlank(message = "Report name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_type_id", foreignKey = @ForeignKey(name = "fk_report_name_type"))
    private ReportType reportType;
}

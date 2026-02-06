package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Master data entity for report types.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "report_types", indexes = @Index(name = "idx_report_types_name", columnList = "name"), uniqueConstraints = @UniqueConstraint(name = "uk_report_type", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportType extends BaseEntity {

    @NotBlank(message = "Report type is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}

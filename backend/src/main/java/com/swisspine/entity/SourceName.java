package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Master data entity for source names used in planner configurations.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "source_names", indexes = @Index(name = "idx_source_names_name", columnList = "name"), uniqueConstraints = @UniqueConstraint(name = "uk_source_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SourceName extends BaseEntity {

    @NotBlank(message = "Source name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}

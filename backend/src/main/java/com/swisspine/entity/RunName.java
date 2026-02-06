package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Master data entity for run names used in planner configurations.
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "run_names", indexes = @Index(name = "idx_run_names_name", columnList = "name"), uniqueConstraints = @UniqueConstraint(name = "uk_run_name", columnNames = "name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RunName extends BaseEntity {

    @NotBlank(message = "Run name is required")
    @Size(max = 255)
    @Column(name = "name", nullable = false, unique = true)
    private String name;
}

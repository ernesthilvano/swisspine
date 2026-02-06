package com.swisspine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Planner with nested relationships.
 * 
 * @author SwissPine Engineering Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlannerDTO {

    private Long id;

    @NotBlank(message = "Planner name is required")
    @Size(max = 255)
    private String name;

    private String description;

    @Size(max = 100)
    private String plannerType;

    private Long externalSystemConfigId;
    private ExternalConnectionDTO externalSystemConfig;

    @Size(max = 50)
    private String status;

    @Size(max = 255)
    private String owner;

    private Instant finishedAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    @Builder.Default
    private List<PlannerFundDTO> funds = new ArrayList<>();

    @Builder.Default
    private List<PlannerSourceDTO> sources = new ArrayList<>();
}

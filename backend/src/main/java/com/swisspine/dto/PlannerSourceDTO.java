package com.swisspine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlannerSourceDTO {
    private Long id;
    private Long sourceNameId;
    private String sourceName;
    private Integer displayOrder;

    @Builder.Default
    private List<PlannerRunDTO> runs = new ArrayList<>();

    @Builder.Default
    private List<PlannerReportDTO> reports = new ArrayList<>();
}

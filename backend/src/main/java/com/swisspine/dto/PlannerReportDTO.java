package com.swisspine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlannerReportDTO {
    private Long id;
    private Long reportTypeId;
    private String reportTypeName;
    private Long reportNameId;
    private String reportName;
    private Integer displayOrder;
}

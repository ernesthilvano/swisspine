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
public class PlannerFundDTO {
    private Long id;
    private Long fundId;
    private String fundName;
    private Long fundAliasId;
    private String fundAliasName;
}

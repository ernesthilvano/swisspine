package com.swisspine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * DTO for master data entities (SourceName, RunName, etc.)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MasterDataDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255)
    private String name;

    private Instant createdAt;
    private Instant updatedAt;
}

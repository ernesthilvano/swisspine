package com.swisspine.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Data Transfer Object for ExternalConnection.
 * 
 * Used for API requests and responses. Excludes sensitive value_field from
 * responses
 * after initial creation (replaced with masked value).
 * 
 * @author SwissPine Engineering Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExternalConnectionDTO {

    private Long id;

    @NotBlank(message = "Connection name is required")
    @Size(max = 255, message = "Connection name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Base URL is required")
    @Size(max = 500, message = "Base URL cannot exceed 500 characters")
    private String baseUrl;

    @NotBlank(message = "Authentication method is required")
    @Size(max = 50, message = "Authentication method cannot exceed 50 characters")
    private String authenticationMethod;

    @NotBlank(message = "Key field is required")
    @Size(max = 255, message = "Key field cannot exceed 255 characters")
    private String keyField;

    /**
     * Value field for authentication.
     * On GET responses, this is masked as "****" if already set.
     * On POST/PUT requests, this is only accepted if not yet set.
     */
    @Size(max = 500, message = "Value field cannot exceed 500 characters")
    private String valueField;

    /**
     * Indicates if the value field has been set.
     * Used by frontend to show/hide masking.
     */
    private Boolean valueFieldSet;

    @Pattern(regexp = "Header|QueryParameters", message = "Authentication place must be either 'Header' or 'QueryParameters'")
    private String authenticationPlace;

    private Boolean isDefault;

    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    /**
     * Masks the value field for security.
     * Called before sending response to client.
     */
    public void maskValueField() {
        if (valueFieldSet != null && valueFieldSet && valueField != null) {
            this.valueField = "********";
        }
    }
}

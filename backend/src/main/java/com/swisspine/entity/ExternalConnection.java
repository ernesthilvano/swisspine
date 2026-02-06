package com.swisspine.entity;

import com.swisspine.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity representing an external system connection configuration.
 * 
 * This entity stores connection details for external systems including
 * authentication methods and credentials. The value_field is sensitive
 * and should be masked in the UI after initial entry.
 * 
 * Business Rules:
 * - Name must be unique across all connections
 * - Only one connection can be marked as default
 * - value_field cannot be modified once set (immutable after first save)
 * - Authentication place must be either "Header" or "QueryParameters"
 * 
 * @author SwissPine Engineering Team
 */
@Entity
@Table(name = "external_connections", indexes = {
        @Index(name = "idx_ext_conn_name", columnList = "name"),
        @Index(name = "idx_ext_conn_default", columnList = "is_default")
}, uniqueConstraints = {
        @UniqueConstraint(name = "uk_ext_conn_name", columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "valueField") // Exclude sensitive data from toString
public class ExternalConnection extends BaseEntity {

    @NotBlank(message = "Connection name is required")
    @Size(max = 255, message = "Connection name cannot exceed 255 characters")
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Base URL is required")
    @Size(max = 500, message = "Base URL cannot exceed 500 characters")
    @Column(name = "base_url", nullable = false, length = 500)
    private String baseUrl;

    @NotBlank(message = "Authentication method is required")
    @Size(max = 50, message = "Authentication method cannot exceed 50 characters")
    @Column(name = "authentication_method", nullable = false, length = 50)
    private String authenticationMethod;

    @NotBlank(message = "Key field is required")
    @Size(max = 255, message = "Key field cannot exceed 255 characters")
    @Column(name = "key_field", nullable = false)
    private String keyField;

    /**
     * Sensitive authentication value.
     * This field should be masked in the UI after initial entry.
     * Once set, it cannot be modified (see valueFieldSet flag).
     */
    @Size(max = 500, message = "Value field cannot exceed 500 characters")
    @Column(name = "value_field", length = 500)
    private String valueField;

    /**
     * Tracks whether the value_field has been set.
     * Used to enforce immutability of the value_field after initial entry.
     */
    @Column(name = "value_field_set", nullable = false)
    @Builder.Default
    private Boolean valueFieldSet = false;

    @Pattern(regexp = "Header|QueryParameters", message = "Authentication place must be either 'Header' or 'QueryParameters'")
    @Column(name = "authentication_place", length = 20)
    private String authenticationPlace;

    /**
     * Indicates if this connection is the default.
     * Only one connection can be default at a time (enforced by database
     * constraint).
     */
    @Column(name = "is_default", nullable = false)
    @Builder.Default
    private Boolean isDefault = false;

    /**
     * Pre-persist hook to set valueFieldSet flag when value is first set.
     */
    @PrePersist
    @PreUpdate
    private void setValueFieldFlag() {
        if (valueField != null && !valueFieldSet) {
            valueFieldSet = true;
        }
    }
}

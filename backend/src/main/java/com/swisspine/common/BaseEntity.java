package com.swisspine.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Base entity class providing common audit fields for all domain entities.
 * 
 * Implements automatic timestamp management through Spring Data JPA auditing.
 * All entities should extend this class to ensure consistent audit trail.
 * 
 * @author SwissPine Engineering Team
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version")
    private Long version;

    /**
     * Determines if this entity is new (not yet persisted).
     * Used by Spring Data JPA to decide between insert and update operations.
     */
    @Transient
    public boolean isNew() {
        return id == null;
    }
}

package com.hcmute.clothingstore.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;

    private String updatedBy;

    @PrePersist
    public void prePersist(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public void preUpdate(){
        this.updatedAt = Instant.now();
    }
}

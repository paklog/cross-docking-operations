package com.paklog.cross.docking.operations.domain.aggregate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * CrossDockOperation Aggregate Root
 */
@Document(collection = "crossdockoperations")
public class CrossDockOperation {

    @Id
    private String id;

    private Instant createdAt;
    private Instant updatedAt;

    // Domain logic methods here


    // Getters
    public String getId() { return id; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
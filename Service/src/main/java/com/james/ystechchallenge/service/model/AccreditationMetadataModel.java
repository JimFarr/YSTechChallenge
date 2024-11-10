package com.james.ystechchallenge.service.model;

import java.time.Instant;
import java.util.UUID;

/**
 * Service model representing accreditation request metadata
 * @author james
 */
public class AccreditationMetadataModel {
    private final UUID id;
    private final Instant dateOfIssue;
    private final UUID latestStatusId;
    private final Instant dateOfStatusAssignment;
    
    public AccreditationMetadataModel(UUID id, Instant dateOfIssue, UUID latestStatusId, Instant dateofStatusAssignment) {
        this.dateOfIssue = dateOfIssue;
        this.dateOfStatusAssignment = dateofStatusAssignment;
        this.id = id;
        this.latestStatusId = latestStatusId;
    }

    public UUID getId() {
        return id;
    }

    public Instant getDateOfIssue() {
        return dateOfIssue;
    }

    public UUID getLatestStatusId() {
        return latestStatusId;
    }

    public Instant getDateOfStatusAssignment() {
        return dateOfStatusAssignment;
    }
}

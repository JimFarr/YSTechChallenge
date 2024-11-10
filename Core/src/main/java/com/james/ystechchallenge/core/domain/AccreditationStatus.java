package com.james.ystechchallenge.core.domain;

import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain model of the status of an accreditation request
 * @author james
 */
public class AccreditationStatus {
    
    private final UUID id;
    private final UUID accreditationId;
    private AccreditationStatusType statusType;
    private final Instant dateOfAssignment;
    
    public AccreditationStatus(UUID accreditationId, AccreditationStatusType statusType, Instant dateOfAssignment) {
        this.id = UUID.randomUUID();
        this.accreditationId = accreditationId;
        this.statusType = statusType;
        this.dateOfAssignment = dateOfAssignment;
    }

    public UUID getId() {
        return id;
    }
    
    public UUID getAccreditationId() {
        return accreditationId;
    }

    public AccreditationStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(AccreditationStatusType statusType) {
        this.statusType = statusType;
    }
    
    public Instant getDateOfAssignment() {
        return dateOfAssignment;
    }
}

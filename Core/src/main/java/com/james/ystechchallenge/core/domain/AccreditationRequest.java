package com.james.ystechchallenge.core.domain;

import com.james.ystechchallenge.core.enumeration.AccreditationRequestType;
import java.time.Instant;
import java.util.UUID;

/**
 * Domain model for an accreditation request
 * @author james
 */
public class AccreditationRequest {
    private final UUID id;
    private final String userId;
    private final AccreditationRequestType type;
    private final UUID accreditationDocumentId;
    private final Instant dateOfIssue;
    
    public AccreditationRequest(String userId, AccreditationRequestType type, UUID accreditationDocumentId, Instant dateOfIssue) {
        this.userId = userId;
        this.type = type;
        this.accreditationDocumentId = accreditationDocumentId;
        this.dateOfIssue = dateOfIssue;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
    
    public String getUserId() {
        return userId;
    }

    public AccreditationRequestType getType() {
        return type;
    }

    public UUID getAccreditationDocumentId() {
        return accreditationDocumentId;
    }
    
    public Instant getDateOfIssue() {
        return dateOfIssue;
    }
}

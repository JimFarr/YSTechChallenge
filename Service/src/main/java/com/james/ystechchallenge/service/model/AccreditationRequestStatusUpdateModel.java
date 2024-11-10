package com.james.ystechchallenge.service.model;

import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import java.util.UUID;

/**
 * Service model representing an intent to update an accreditation request
 * @author james
 */
public class AccreditationRequestStatusUpdateModel {
    private final UUID accreditationId;
    private final AccreditationStatusType updatedStatus;
    
    public AccreditationRequestStatusUpdateModel(UUID accreditationId, AccreditationStatusType updatedStatus) {
        this.accreditationId = accreditationId;
        this.updatedStatus = updatedStatus;
    }

    public UUID getAccreditationId() {
        return accreditationId;
    }

    public AccreditationStatusType getUpdatedStatus() {
        return updatedStatus;
    }
}

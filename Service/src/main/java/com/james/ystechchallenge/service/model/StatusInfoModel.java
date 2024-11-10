package com.james.ystechchallenge.service.model;

import com.james.ystechchallenge.core.enumeration.AccreditationRequestType;
import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import java.util.UUID;

/**
 * Service model representing the status of an accreditation request
 * @author james
 */
public class StatusInfoModel {
    private final UUID statusId;
    private final AccreditationRequestType accreditationType;
    private final AccreditationStatusType statusType;
    
    public StatusInfoModel(UUID statusId, AccreditationRequestType accreditationType, AccreditationStatusType statusType) {
        this.statusId = statusId;
        this.accreditationType = accreditationType;
        this.statusType = statusType;
    }

    public UUID getStatusId() {
        return statusId;
    }

    public AccreditationRequestType getAccreditationType() {
        return accreditationType;
    }

    public AccreditationStatusType getStatusType() {
        return statusType;
    }
}

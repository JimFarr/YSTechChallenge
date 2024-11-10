package com.james.ystechchallenge.data.abstraction;

import com.james.ystechchallenge.core.domain.AccreditationStatus;
import java.util.List;
import java.util.UUID;

/**
 * Represents a data source exposing operations on stored accreditation request statuses
 * @author james
 */
public interface AccreditationStatusRepository {
    
    /**
     * Saves an accreditation request status
     * @param status the status being saved
     */
    void save(AccreditationStatus status);
    
    /**
     * Locates all the statuses for a given accreditation request
     * @param accreditationId the id of the accreditation in question
     * @return a list of accreditation request statuses
     */
    List<AccreditationStatus> findByAccreditationId(UUID accreditationId);
}

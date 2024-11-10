package com.james.ystechchallenge.data.abstraction;

import com.james.ystechchallenge.core.domain.AccreditationRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Represents a data source exposing operations on stored accreditation requests
 * @author james
 */
public interface AccreditationRequestRepository {
    
    /**
     * Saves an accreditation request
     * @param request the accreditation request to be saved
     */
    void save(AccreditationRequest request);
    
    /**
     * Locates all the accreditation requests made by a given user
     * @param userId the id of the user being used to locate the requests
     * @return a list of accreditation requests
     */
    List<AccreditationRequest> findByUserId(String userId);
    
    /**
     * Locates a single accreditation request by id
     * @param accreditationId the id of the accreditation request being located
     * @return the accreditation request, or empty if not found
     */
    Optional<AccreditationRequest> findById(UUID accreditationId);
    
    /**
     * Get all accreditation requests
     * @return a list of accreditation requests
     */
    List<AccreditationRequest> getAll();
}

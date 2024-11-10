package com.james.ystechchallenge.service.abstraction;

import com.james.ystechchallenge.service.model.AccreditationMetadataModel;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import com.james.ystechchallenge.service.model.StatusInfoModel;
import java.util.List;
import java.util.UUID;

/**
 * Service pertaining to operations on accreditation requests
 * @author james
 */
public interface AccreditationService {

    /**
     * Gets the metadata of all accreditation requests with confirmed status
     * @return a list of metadata models wrapped in a service result
     */
    ServiceResult<List<AccreditationMetadataModel>> getAllConfirmedAccreditationMetadata();

    /**
     * Creates a new accreditation request in pending status
     * @param model the model informing the creation process
     * @return the UUID of the created request wrapped in a service result
     */
    ServiceResult<UUID> registerAccreditationRequest(AccreditationRequestCreateModel model);

    /**
     * Updates an existing accreditation request
     * @param model the model informing the update process
     * @return the UUID of the updated request wrapped in a service result
     */
    ServiceResult<UUID> updateAccreditationRequestStatus(AccreditationRequestStatusUpdateModel model);
    
    /**
     * Gets the status of all recorded accreditation requests for a given user
     * @param userId id of the user in question
     * @return a list of info models wrapped in a service result
     */
    ServiceResult<List<StatusInfoModel>> getStatusInfoForUser(String userId);
}

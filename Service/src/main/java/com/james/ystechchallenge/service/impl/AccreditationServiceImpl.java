package com.james.ystechchallenge.service.impl;

import com.james.ystechchallenge.core.domain.AccreditationRequest;
import com.james.ystechchallenge.core.domain.AccreditationStatus;
import com.james.ystechchallenge.core.domain.Document;
import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import com.james.ystechchallenge.core.enumeration.FailureReason;
import com.james.ystechchallenge.data.abstraction.AccreditationRequestRepository;
import com.james.ystechchallenge.data.abstraction.AccreditationStatusRepository;
import com.james.ystechchallenge.data.abstraction.DocumentRepository;
import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.mapping.ServiceModelToEntityMapper;
import com.james.ystechchallenge.service.model.AccreditationMetadataModel;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import com.james.ystechchallenge.service.model.StatusInfoModel;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccreditationServiceImpl implements AccreditationService {
    private final DocumentRepository documentRepo;
    private final AccreditationRequestRepository accreditationRequestRepo;
    private final AccreditationStatusRepository accreditationStatusRepo;

    private final ServiceModelToEntityMapper entityMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(AccreditationServiceImpl.class);
    
    @Autowired
    public AccreditationServiceImpl(DocumentRepository documentRepo, AccreditationRequestRepository accreditationRequestRepo, AccreditationStatusRepository accreditationStatusRepo, ServiceModelToEntityMapper entityMapper) {
        this.documentRepo = documentRepo;
        this.accreditationRequestRepo = accreditationRequestRepo;
        this.accreditationStatusRepo = accreditationStatusRepo;
        this.entityMapper = entityMapper;
    }
    
    @Override
    public ServiceResult<List<AccreditationMetadataModel>> getAllConfirmedAccreditationMetadata() {
        List<AccreditationMetadataModel> metadata = new ArrayList<>();
        
        List<AccreditationRequest> requests = accreditationRequestRepo.getAll();
        for (AccreditationRequest request : requests) {
            Optional<AccreditationStatus> status = getLatestStatusForAccreditation(request);
            
            if (!status.isPresent() || status.get().getStatusType() != AccreditationStatusType.CONFIRMED) {
                continue;
            }
            
            metadata.add(new AccreditationMetadataModel(request.getId(), request.getDateOfIssue(), status.get().getId(), status.get().getDateOfAssignment()));
        }
        
        return ServiceResult.success(metadata);
    }

    @Override
    @Transactional
    public ServiceResult<UUID> registerAccreditationRequest(AccreditationRequestCreateModel model) {
        
        if (doesUserHaveActiveRequests(model.getUserId())) {
            return ServiceResult.failure(FailureReason.EXISTING_REQUEST);
        }
        
        Document doc = entityMapper.toEntity(model.getDocument());
        AccreditationRequest request = new AccreditationRequest(model.getUserId(), model.getAccreditationType(), doc.getId(), Instant.now());
        AccreditationStatus status = new AccreditationStatus(request.getId(), AccreditationStatusType.PENDING, request.getDateOfIssue());
        
        // ideally save these in a single transaction using a unit of work
        documentRepo.save(doc);
        accreditationRequestRepo.save(request);
        accreditationStatusRepo.save(status);
        
        return ServiceResult.success(request.getId());
    }

    @Override
    public ServiceResult<UUID> updateAccreditationRequestStatus(AccreditationRequestStatusUpdateModel model) {
        Optional<AccreditationRequest> accreditationRequestToUpdate = accreditationRequestRepo.findById(model.getAccreditationId());

        if (!accreditationRequestToUpdate.isPresent()) {
            return ServiceResult.failure(FailureReason.SOURCE_ITEM_NOT_FOUND);
        }
        
        Optional<AccreditationStatus> latestStatus = getLatestStatusForAccreditation(accreditationRequestToUpdate.get());        
        
        if (!latestStatus.isPresent()) {
            return ServiceResult.failure(FailureReason.MISSING_DATA);
        }
        
        if (!isValidStateTransition(latestStatus.get().getStatusType(), model.getUpdatedStatus())) {
            return ServiceResult.failure(FailureReason.INVALID_STATE_TRANSITION);
        }
        
        AccreditationStatus status = new AccreditationStatus(model.getAccreditationId(), model.getUpdatedStatus(), Instant.now());
        accreditationStatusRepo.save(status);
        
        return ServiceResult.success(accreditationRequestToUpdate.get().getId());
    }
    
    @Override
    public ServiceResult<List<StatusInfoModel>> getStatusInfoForUser(String userId) {
        List<StatusInfoModel> models = new ArrayList<>();
        
        List<AccreditationRequest> requests = accreditationRequestRepo.findByUserId(userId);
        
        if (requests.isEmpty()) {
            return ServiceResult.failure(FailureReason.SOURCE_ITEM_NOT_FOUND);
        }
        
        for (AccreditationRequest request : requests) {
            Optional<AccreditationStatus> status = getLatestStatusForAccreditation(request);
            
            if (!status.isPresent()) {
                logger.error(String.format("no status found for request with id %s", request.getId()));
                continue;
            }
            
            models.add(new StatusInfoModel(status.get().getId(), request.getType(), status.get().getStatusType()));
        }
        
        return ServiceResult.success(models);
    }
    
    /**
     * Determines if the given user has any active (pending) requests
     * @param userId the id of the user being checked
     * @return true if the user has pending requests
     */
    private Boolean doesUserHaveActiveRequests(String userId) {
        Optional<AccreditationRequest> mostRecentRequest = getLatestAccreditationRequestForUser(userId);
        
        if (!mostRecentRequest.isPresent()) {
            return false;
        }
        
        Optional<AccreditationStatus> mostRecentStatus = getLatestStatusForAccreditation(mostRecentRequest.get());
        
        if (!mostRecentStatus.isPresent()) {
            return false;
        }
        
        return mostRecentStatus.get().getStatusType() == AccreditationStatusType.PENDING;
    }
    
    /**
     * Retrieves the latest status for a given accreditation request
     * @param request the accreditation request
     * @return the status for that request
     */
    private Optional<AccreditationStatus> getLatestStatusForAccreditation(AccreditationRequest request) {
        List<AccreditationStatus> statusesByAccreditationId = accreditationStatusRepo.findByAccreditationId(request.getId());        
        
        Optional<AccreditationStatus> mostRecentStatus = statusesByAccreditationId.stream().max(Comparator.comparing(AccreditationStatus::getDateOfAssignment));
        
        if (!mostRecentStatus.isPresent()) {
            logger.error("missing data"); // this is an exceptional issue, but we should handle it using logging and our ServiceResult infrastructure
        }
        
        return mostRecentStatus;
    }
    
    /**
     * Retrieves the latest accreditation request for a given user
     * @param userId the user in question
     * @return the latest accreditation request
     */
    private Optional<AccreditationRequest> getLatestAccreditationRequestForUser(String userId) {
        List<AccreditationRequest> accreditationRequestsByUser = accreditationRequestRepo.findByUserId(userId);
        
        if (accreditationRequestsByUser.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<AccreditationRequest> mostRecentRequest = accreditationRequestsByUser.stream().max(Comparator.comparing(AccreditationRequest::getDateOfIssue));
        return mostRecentRequest;
    }
    
    
    /**
     * Checks if a state transition is valid:
     * FAILED cannot be updated
     * CONFIRMED can only go to expired
     * PENDING can go to CONFIRMED or FAILED (we will be accepting EXPIRED as there is no spec against this)
     * @param oldStatusType the old status type
     * @param newStatusType the new status type
     * @return true if the transition is valid
     */
    private Boolean isValidStateTransition(AccreditationStatusType oldStatusType, AccreditationStatusType newStatusType) {
        
        if (oldStatusType == newStatusType) {
            return false; // we shouldn't be creating new statuses if they are the same as previous
        }
        
        return switch (oldStatusType) {
            case FAILED -> false;
            case CONFIRMED -> newStatusType == AccreditationStatusType.EXPIRED;
            case PENDING -> true;
            case EXPIRED -> false;
            default -> false;
        };
    }
}

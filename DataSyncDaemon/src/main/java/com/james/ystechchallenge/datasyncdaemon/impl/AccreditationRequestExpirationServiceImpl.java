package com.james.ystechchallenge.datasyncdaemon.impl;

import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import com.james.ystechchallenge.datasyncdaemon.abstractions.AccreditationRequestExpirationService;
import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.model.AccreditationMetadataModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AccreditationRequestExpirationServiceImpl implements AccreditationRequestExpirationService{
    
    private final AccreditationService service;
    private final int expirationDurationSeconds;
    
    private static final Logger logger = LoggerFactory.getLogger(AccreditationRequestExpirationServiceImpl.class);
    
    @Autowired
    public AccreditationRequestExpirationServiceImpl(AccreditationService service, int expirationDurationSeconds) {
        this.service = service;
        this.expirationDurationSeconds = expirationDurationSeconds;
    }
    
    @Override
    public void updateExpiredStatuses() {
        ServiceResult<List<AccreditationMetadataModel>> result = service.getAllConfirmedAccreditationMetadata();
        
        if (!result.isSuccess()) {
            logger.error("something went wrong while updating expired statuses");
            return;
        }
        
        List<AccreditationMetadataModel> models = result.getValue();
        
        for (AccreditationMetadataModel model : models) {           
            
            long secondsBetween = ChronoUnit.SECONDS.between(model.getDateOfStatusAssignment(), Instant.now());
            
            if (secondsBetween < expirationDurationSeconds) {
                continue; // not yet expired
            }
            
            AccreditationRequestStatusUpdateModel updateModel = new AccreditationRequestStatusUpdateModel(model.getId(), AccreditationStatusType.EXPIRED);            
            ServiceResult<UUID> updateResult = service.updateAccreditationRequestStatus(updateModel);
            
            if (!updateResult.isSuccess()) {
                logger.error(String.format("unable to expire accreditation with id %s", model.getId()));
            }
        }
    }
}

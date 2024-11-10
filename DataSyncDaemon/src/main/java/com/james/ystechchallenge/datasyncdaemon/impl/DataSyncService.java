package com.james.ystechchallenge.datasyncdaemon.impl;

import com.james.ystechchallenge.datasyncdaemon.abstractions.AccreditationRequestExpirationService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import com.james.ystechchallenge.datasyncdaemon.config.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scheduled service that triggers a data sync event
 * @author james
 */
@Component
public class DataSyncService extends QuartzJobBean {
    private AccreditationRequestExpirationService service;
    private static final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
    
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        
        if(this.service == null) {
            this.service = ServiceLocator.getBean(AccreditationRequestExpirationService.class);
        }
        
        logger.info("Starting automatic expiration of old accreditation requests");
        this.service.updateExpiredStatuses();
        logger.info("Automatic expiration of old accreditation requests has been completed");
        
        // potentially add a notification service here to send word of the expired requests to the relevant admins/users
    }
}

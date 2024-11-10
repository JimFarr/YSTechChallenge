package com.james.ystechchallenge.datasyncdaemon.abstractions;

/**
 * Service that deals with the expiration of accreditation requests
 * @author james
 */
public interface AccreditationRequestExpirationService {
    
    /**
     * Updates the statuses of accreditation requests that have passed their expiry date
     */
    void updateExpiredStatuses();
}

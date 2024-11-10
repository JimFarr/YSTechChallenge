package com.james.ystechchallenge.service.model;

import com.james.ystechchallenge.core.enumeration.FailureReason;

/**
 * A wrapper class allowing for graceful error handling
 * @author james
 * @param <T> 
 */
public class ServiceResult<T> {

    private boolean success;
    private T value;
    private FailureReason failureReason;

    public boolean isSuccess() {
        return success;
    }

    private void setSuccess(boolean success) {
        this.success = success;
    }

    public T getValue() {
        return value;
    }

    private void setValue(T value) {
        this.value = value;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    private void setFailureReason(FailureReason failureReason) {
        this.failureReason = failureReason;
    }
    
    /**
     * Indicates a successful execution of a service method
     * @param <T>
     * @param value
     * @return 
     */
    public static <T> ServiceResult<T> success(T value) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(true);
        result.setValue(value);
        return result;
    }
    
    /**
     * Indicates a failed execution of a service method
     * @param <T>
     * @param failureReason
     * @return 
     */
    public static <T> ServiceResult<T> failure(FailureReason failureReason) {
        ServiceResult<T> result = new ServiceResult<>();
        result.setSuccess(false);
        result.setFailureReason(failureReason);
        return result;
    }
}

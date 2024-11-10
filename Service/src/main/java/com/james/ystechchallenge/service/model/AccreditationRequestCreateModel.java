package com.james.ystechchallenge.service.model;

import com.james.ystechchallenge.core.enumeration.AccreditationRequestType;

/**
 * Service model representing an intent to create an accreditation request
 * @author james
 */
public class AccreditationRequestCreateModel {
    private String userId;
    private AccreditationRequestType accreditationType;
    private DocumentCreateModel document;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public AccreditationRequestType getAccreditationType() {
        return accreditationType;
    }

    public void setAccreditationType(AccreditationRequestType accreditationType) {
        this.accreditationType = accreditationType;
    }

    public DocumentCreateModel getDocument() {
        return document;
    }

    public void setDocument(DocumentCreateModel document) {
        this.document = document;
    }
}

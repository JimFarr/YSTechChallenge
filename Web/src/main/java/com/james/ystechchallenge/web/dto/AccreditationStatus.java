package com.james.ystechchallenge.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Accreditation information and status")
public class AccreditationStatus {

    @Schema(description = "The type of accreditation")
    private String accreditationType;
    
    @Schema(description = "Latest status of the accreditation")
    private String status;

    public String getAccreditationType() {
        return accreditationType;
    }

    public void setAccreditationType(String accreditationType) {
        this.accreditationType = accreditationType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

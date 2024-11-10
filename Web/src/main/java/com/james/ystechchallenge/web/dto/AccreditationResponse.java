package com.james.ystechchallenge.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.UUID;

@Schema(description = "User accreditation information and status")
public class AccreditationResponse {

    @Schema(description = "Id of the user owning the accreditation")
    private String userId;
    
    @Schema(description = "Accreditations and their latest statuses")
    private Map<UUID, AccreditationStatus> accreditationStatuses;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<UUID, AccreditationStatus> getAccreditationStatuses() {
        return accreditationStatuses;
    }

    public void setAccreditationStatuses(Map<UUID, AccreditationStatus> accreditationStatuses) {
        this.accreditationStatuses = accreditationStatuses;
    }
}

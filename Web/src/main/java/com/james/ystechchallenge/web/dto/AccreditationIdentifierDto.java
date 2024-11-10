package com.james.ystechchallenge.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Identification for an accreditation request")
public class AccreditationIdentifierDto {
    
    @Schema(description = "The id of the accreditation request")
    private final UUID accreditationId;
    
    public AccreditationIdentifierDto(UUID accreditationId) {
        this.accreditationId = accreditationId;
    }
    
    public UUID getAccreditationId() {
        return accreditationId;
    }
}

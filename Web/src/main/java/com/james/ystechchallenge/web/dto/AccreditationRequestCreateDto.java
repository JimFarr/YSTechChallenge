package com.james.ystechchallenge.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.james.ystechchallenge.web.validation.ValidAccreditationType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "A request to register accreditation for a specific user")
public class AccreditationRequestCreateDto {

    @Schema(description = "Id of the user requesting accreditation")
    @NotBlank(message = "'UserId' is a mandatory field")
    @JsonProperty("user_id")
    private String userId;
    
    @Schema(description = "Type of accreditation being requested", allowableValues = {"BY_INCOME", "BY_NET_WORTH"}, example = "BY_INCOME")
    @ValidAccreditationType
    @JsonProperty("accreditation_type")
    private String accreditationType;
    
    @Schema(description = "A reviewable document proposing a reasonable argument in favour of accreditation, as per the accreditation type", required = true)
    @NotNull(message = "'Document' is a mandatory field")
    @JsonProperty("document")
    private DocumentCreateDto document;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccreditationType() {
        return accreditationType;
    }

    public void setAccreditationType(String accreditationType) {
        this.accreditationType = accreditationType;
    }

    public DocumentCreateDto getDocument() {
        return document;
    }

    public void setDocument(DocumentCreateDto document) {
        this.document = document;
    }
}

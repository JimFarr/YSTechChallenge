package com.james.ystechchallenge.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.james.ystechchallenge.web.validation.ValidAccreditationOutcome;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "The outcome status of an accreditation request")
public class OutcomeStatusDto {
    
    @Schema(description = "Outcome of the accreditation", allowableValues = {"CONFIRMED", "EXPIRED", "FAILED"}, example = "CONFIRMED")
    @NotBlank(message = "'Outcome' is a mandatory field")
    @ValidAccreditationOutcome
    @JsonProperty("outcome")
    private String outcome;

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
package com.james.ystechchallenge.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AccreditationOutcomeValidator implements ConstraintValidator<ValidAccreditationOutcome, String> {

    private final List<String> validOutcomes = Arrays.asList("CONFIRMED", "EXPIRED", "FAILED");

    @Override
    public void initialize(ValidAccreditationOutcome constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return validOutcomes.contains(value);
    }

}

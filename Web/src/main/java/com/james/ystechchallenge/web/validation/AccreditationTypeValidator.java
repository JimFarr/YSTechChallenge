package com.james.ystechchallenge.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class AccreditationTypeValidator implements ConstraintValidator<ValidAccreditationType, String> {

    private final List<String> validTypes = Arrays.asList("BY_INCOME", "BY_NET_WORTH");

    @Override
    public void initialize(ValidAccreditationType constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return validTypes.contains(value);
    }

}

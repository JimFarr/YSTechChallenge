package com.james.ystechchallenge.web.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AccreditationOutcomeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidAccreditationOutcome {

    String message() default "Invalid accreditation outcome";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload () default {};
}

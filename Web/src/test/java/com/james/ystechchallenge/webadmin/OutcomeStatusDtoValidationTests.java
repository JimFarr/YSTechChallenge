package com.james.ystechchallenge.web;

import com.james.ystechchallenge.web.dto.OutcomeStatusDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class OutcomeStatusDtoValidationTests {
        private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @ParameterizedTest
    @CsvSource({
        "CONFIRMED",
        "EXPIRED",
        "FAILED"
    })
    public void noViolationsWithCorrectStatus(String status) {
        OutcomeStatusDto dto = new OutcomeStatusDto();
        dto.setOutcome(status);
        
        Set<ConstraintViolation<OutcomeStatusDto>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
    
    @ParameterizedTest
    @CsvSource({
        "PENDING",
        "CONFIRMED1",
        "EXP",
        "failed"
    })
    public void statusViolations(String status) {
        OutcomeStatusDto dto = new OutcomeStatusDto();
        dto.setOutcome(status);
        
        Set<ConstraintViolation<OutcomeStatusDto>> violations = validator.validate(dto);
        assertEquals(1, violations.size());
    }
}

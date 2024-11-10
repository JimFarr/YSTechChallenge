package com.james.ystechchallenge.web;

import com.james.ystechchallenge.web.dto.AccreditationRequestCreateDto;
import com.james.ystechchallenge.web.dto.DocumentCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DocumentCreateDtoValidationTests {
    private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @Test
    public void noViolationsWithAccreditationType() {
        DocumentCreateDto document = new DocumentCreateDto();
        document.setContent("sampleContent");
        document.setMimeType("application/pdf");
        document.setName("testName");
        
        Set<ConstraintViolation<DocumentCreateDto>> violations = validator.validate(document);
        assertTrue(violations.isEmpty());
    }
    
    @Test
    public void violationWithBlankProperties() {
        DocumentCreateDto document = new DocumentCreateDto();
        document.setContent("");
        document.setMimeType("");
        document.setName("");
        
        Set<ConstraintViolation<DocumentCreateDto>> violations = validator.validate(document);
        assertEquals(3, violations.size());
    }
}

package com.james.ystechchallenge.web;

import com.james.ystechchallenge.web.dto.AccreditationRequestCreateDto;
import com.james.ystechchallenge.web.dto.DocumentCreateDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class AccreditationRequestCreateDtoValidationTests {
    private Validator validator;
    
    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    @ParameterizedTest
    @CsvSource({
        "BY_INCOME",
        "BY_NET_WORTH"
    })
    public void noViolationsWithAccreditationType(String accreditationType) {
        AccreditationRequestCreateDto input = new AccreditationRequestCreateDto();
        input.setUserId("userid1");
        input.setAccreditationType(accreditationType);
        
        DocumentCreateDto document = new DocumentCreateDto();
        document.setContent("sampleContent");
        document.setMimeType("application/pdf");
        document.setName("testName");
        
        input.setDocument(document);
        
        Set<ConstraintViolation<AccreditationRequestCreateDto>> violations = validator.validate(input);
        assertTrue(violations.isEmpty());
    }
    
    @ParameterizedTest
    @CsvSource({
        "BY_INCOME1",
        "BY_NET__WORTH"
    })
    public void violationsWithAccreditationType(String accreditationType) {
        AccreditationRequestCreateDto input = new AccreditationRequestCreateDto();
        input.setUserId("userid1");
        input.setAccreditationType(accreditationType);
        
        DocumentCreateDto document = new DocumentCreateDto();
        document.setContent("sampleContent");
        document.setMimeType("application/pdf");
        document.setName("testName");
        
        input.setDocument(document);
        
        Set<ConstraintViolation<AccreditationRequestCreateDto>> violations = validator.validate(input);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
    
    @Test
    public void violationsWithBlankId() {
        AccreditationRequestCreateDto input = new AccreditationRequestCreateDto();
        input.setUserId("");
        input.setAccreditationType("BY_INCOME");
        
        DocumentCreateDto document = new DocumentCreateDto();
        document.setContent("sampleContent");
        document.setMimeType("application/pdf");
        document.setName("testName");
        
        input.setDocument(document);
        
        Set<ConstraintViolation<AccreditationRequestCreateDto>> violations = validator.validate(input);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
    
    @Test
    public void violationsWithNullDocument() {
        AccreditationRequestCreateDto input = new AccreditationRequestCreateDto();
        input.setUserId("userid1");
        input.setAccreditationType("BY_INCOME");        
        input.setDocument(null);
        
        Set<ConstraintViolation<AccreditationRequestCreateDto>> violations = validator.validate(input);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
    }
}

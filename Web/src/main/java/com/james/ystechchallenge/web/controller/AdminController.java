package com.james.ystechchallenge.web.controller;

import static com.james.ystechchallenge.core.enumeration.FailureReason.EXISTING_REQUEST;
import static com.james.ystechchallenge.core.enumeration.FailureReason.INVALID_STATE_TRANSITION;
import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import com.james.ystechchallenge.web.dto.AccreditationIdentifierDto;
import com.james.ystechchallenge.web.dto.AccreditationRequestCreateDto;
import com.james.ystechchallenge.web.dto.OutcomeStatusDto;
import com.james.ystechchallenge.web.mapping.DtoToServiceModelMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "User Accreditation Management API", description = "Exposes operations relating to user accreditation. This API is Admin-only.")
public class AdminController {
    private final DtoToServiceModelMapper mapper;
    private final AccreditationService service;
    
    @Autowired
    public AdminController(DtoToServiceModelMapper mapper, AccreditationService service) {
        this.mapper = mapper;
        this.service = service;
    }
    
    @PostMapping("/accreditation")
    @Operation(summary = "Register an accreditation for a given user")
    public ResponseEntity applyAccreditationForUser(@Valid @RequestBody AccreditationRequestCreateDto accreditationRequest) {
        AccreditationRequestCreateModel createModel = mapper.toCreateModel(accreditationRequest);
        ServiceResult<UUID> result = service.registerAccreditationRequest(createModel);
        
        if (result.isSuccess()) {
            // 201 CREATED
            return ResponseEntity.status(HttpStatus.CREATED).body(new AccreditationIdentifierDto(result.getValue()));
        }
        
        return switch (result.getFailureReason()) {
            case EXISTING_REQUEST -> ResponseEntity.status(HttpStatus.CONFLICT).body("user has an active accreditation request");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an unexpected error occurred, please contact a system administrator");
        };
    }
    
    @PutMapping("/accreditation/{accreditationId}")
    @Operation(summary = "Update the accreditation status for a given user")
    public ResponseEntity updateAccreditationForUser(@PathVariable UUID accreditationId, @Valid @RequestBody OutcomeStatusDto outcomeStatus) {
       AccreditationRequestStatusUpdateModel updateModel = mapper.toUpdateModel(outcomeStatus, accreditationId);
       ServiceResult<UUID> result = service.updateAccreditationRequestStatus(updateModel);
       
       if (result.isSuccess()) {
           // 200 OK
           return ResponseEntity.status(HttpStatus.OK).body(new AccreditationIdentifierDto(result.getValue()));
       }
       
       return switch (result.getFailureReason()) {
            case INVALID_STATE_TRANSITION -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("state change request is invalid");
            case SOURCE_ITEM_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("the requested resource was not found");
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an unexpected error occurred, please contact a system administrator");
        };
    }
}

package com.james.ystechchallenge.web.controller;

import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.model.ServiceResult;
import com.james.ystechchallenge.service.model.StatusInfoModel;
import com.james.ystechchallenge.web.dto.AccreditationResponse;
import com.james.ystechchallenge.web.dto.AccreditationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
// ideally since this is a client-facing API, we version it as a v1 @RequestMapping("v1/user") for better backwards-compatibility
@RequestMapping("user")
@Tag(name = "User Accreditation Monitoring API", description = "Exposes operations relating to viewing user accreditations.")
public class V1ClientController {
    
    private final AccreditationService service;
    
    @Autowired
    public V1ClientController(AccreditationService service) {
        this.service = service;
    }

    @GetMapping("/{userId}/accreditation")
    @Operation(summary = "Retrieves accreditation for a given user")
    public ResponseEntity getUserAccreditation(@PathVariable String userId) {
        
        ServiceResult<List<StatusInfoModel>> result = service.getStatusInfoForUser(userId);
        
        if (!result.isSuccess()) {
            return switch(result.getFailureReason()) {
                case SOURCE_ITEM_NOT_FOUND -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("requested data was not found");
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("an unexpected error occurred, please contact a system administrator");
            };
        }
        
        Map<UUID, AccreditationStatus> statusMapping = new HashMap<>();
        
        for (StatusInfoModel model : result.getValue()) {
            
            AccreditationStatus status = new AccreditationStatus();
            status.setAccreditationType(model.getAccreditationType().toString());
            status.setStatus(model.getStatusType().toString());
            
            statusMapping.put(model.getStatusId(), status);
        }
        
        AccreditationResponse response = new AccreditationResponse();
        response.setUserId(userId);
        response.setAccreditationStatuses(statusMapping);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

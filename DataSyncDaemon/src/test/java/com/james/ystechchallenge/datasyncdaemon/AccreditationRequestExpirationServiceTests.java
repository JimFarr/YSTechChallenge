package com.james.ystechchallenge.datasyncdaemon;

import com.james.ystechchallenge.datasyncdaemon.abstractions.AccreditationRequestExpirationService;
import com.james.ystechchallenge.datasyncdaemon.impl.AccreditationRequestExpirationServiceImpl;
import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.model.AccreditationMetadataModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccreditationRequestExpirationServiceTests {
    
    private AccreditationService accreditationService;
    private AccreditationRequestExpirationService service;
    private final int EXPIRATION_SECONDS = 30 * 24 * 60 * 60;

    @BeforeEach
    public void setUp() {
        accreditationService = Mockito.spy(AccreditationService.class);
        service = new AccreditationRequestExpirationServiceImpl(accreditationService, EXPIRATION_SECONDS);
    }
    
    @Test
    public void expirationRoutineRunsCorrectlyOnConfirmedStatuses() {
        Instant now = Instant.now();
        
        List<AccreditationMetadataModel> statuses = new ArrayList<>();
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(29, ChronoUnit.DAYS)));
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(30, ChronoUnit.DAYS)));
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(31, ChronoUnit.DAYS)));
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(EXPIRATION_SECONDS - 1, ChronoUnit.SECONDS)));
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(EXPIRATION_SECONDS, ChronoUnit.SECONDS)));
        statuses.add(new AccreditationMetadataModel(UUID.randomUUID(), now, UUID.randomUUID(), now.minus(EXPIRATION_SECONDS + 1, ChronoUnit.SECONDS)));
        
        ServiceResult<List<AccreditationMetadataModel>> result = ServiceResult.success(statuses);
        
        when(accreditationService.updateAccreditationRequestStatus(any(AccreditationRequestStatusUpdateModel.class))).thenReturn(ServiceResult.success(UUID.randomUUID()));
        
        when(accreditationService.getAllConfirmedAccreditationMetadata()).thenReturn(result);
        service.updateExpiredStatuses();
        
        ArgumentCaptor<AccreditationRequestStatusUpdateModel> argumentCaptor = ArgumentCaptor.forClass(AccreditationRequestStatusUpdateModel.class);
        
        verify(accreditationService, times(4)).updateAccreditationRequestStatus(argumentCaptor.capture());
        List<AccreditationRequestStatusUpdateModel> capturedArguments = argumentCaptor.getAllValues();
        
        
        assertEquals(4, capturedArguments.size());
        assertEquals(statuses.get(1).getId(), capturedArguments.get(0).getAccreditationId());
        assertEquals(statuses.get(2).getId(), capturedArguments.get(1).getAccreditationId());
        assertEquals(statuses.get(4).getId(), capturedArguments.get(2).getAccreditationId());
        assertEquals(statuses.get(5).getId(), capturedArguments.get(3).getAccreditationId());
    }   
}

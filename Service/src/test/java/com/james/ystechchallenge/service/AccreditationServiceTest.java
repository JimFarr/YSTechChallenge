package com.james.ystechchallenge.service;

import com.james.ystechchallenge.core.domain.AccreditationRequest;
import com.james.ystechchallenge.core.domain.AccreditationStatus;
import com.james.ystechchallenge.core.enumeration.AccreditationRequestType;
import com.james.ystechchallenge.core.enumeration.AccreditationStatusType;
import com.james.ystechchallenge.core.enumeration.FailureReason;
import com.james.ystechchallenge.data.abstraction.AccreditationRequestRepository;
import com.james.ystechchallenge.data.abstraction.AccreditationStatusRepository;
import com.james.ystechchallenge.data.abstraction.DocumentRepository;
import com.james.ystechchallenge.service.abstraction.AccreditationService;
import com.james.ystechchallenge.service.impl.AccreditationServiceImpl;
import com.james.ystechchallenge.service.mapping.ServiceModelToEntityMapper;
import com.james.ystechchallenge.service.model.AccreditationMetadataModel;
import com.james.ystechchallenge.service.model.AccreditationRequestCreateModel;
import com.james.ystechchallenge.service.model.AccreditationRequestStatusUpdateModel;
import com.james.ystechchallenge.service.model.DocumentCreateModel;
import com.james.ystechchallenge.service.model.ServiceResult;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class AccreditationServiceTest {

    private AccreditationRequestRepository requestRepo;
    private AccreditationStatusRepository statusRepo;
    private DocumentRepository docRepo;
    private ServiceModelToEntityMapper mapper;
    private AccreditationService service;

    @BeforeEach
    public void setUp() {
        requestRepo = Mockito.mock(AccreditationRequestRepository.class);
        statusRepo = Mockito.mock(AccreditationStatusRepository.class);
        docRepo = Mockito.mock(DocumentRepository.class);

        mapper = Mappers.getMapper(ServiceModelToEntityMapper.class);
        service = new AccreditationServiceImpl(docRepo, requestRepo, statusRepo, mapper);
    }

    @ParameterizedTest
    @CsvSource({
        "FAILED, FAILED",
        "FAILED, CONFIRMED",
        "FAILED, EXPIRED",
        "FAILED, PENDING",
        "CONFIRMED, CONFIRMED",
        "CONFIRMED, FAILED",
        "CONFIRMED, PENDING",
        "PENDING, PENDING",
        "EXPIRED, EXPIRED",
        "EXPIRED, CONFIRMED",
        "EXPIRED, PENDING",
        "EXPIRED, FAILED"
    })
    public void invalidStateTransitions(String from, String to) {
        AccreditationRequest existingRequest = new AccreditationRequest("testUserId", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), Instant.now());
        when(requestRepo.findById(existingRequest.getId())).thenReturn(Optional.of(existingRequest));

        AccreditationStatus latestStatus = new AccreditationStatus(existingRequest.getId(), AccreditationStatusType.valueOf(from), Instant.now());
        List<AccreditationStatus> statuses = new ArrayList<>();
        statuses.add(latestStatus);
        when(statusRepo.findByAccreditationId(existingRequest.getId())).thenReturn(statuses);

        AccreditationRequestStatusUpdateModel model = new AccreditationRequestStatusUpdateModel(existingRequest.getId(), AccreditationStatusType.valueOf(to));

        ServiceResult result = service.updateAccreditationRequestStatus(model);
        assertFalse(result.isSuccess());
        assertEquals(FailureReason.INVALID_STATE_TRANSITION, result.getFailureReason());
    }

    @ParameterizedTest
    @CsvSource({
        "PENDING, FAILED",
        "PENDING, CONFIRMED",
        "PENDING, EXPIRED",
        "CONFIRMED, EXPIRED",})
    public void validStateTransitions(String from, String to) {
        AccreditationRequest existingRequest = new AccreditationRequest("testUserId", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), Instant.now());
        when(requestRepo.findById(existingRequest.getId())).thenReturn(Optional.of(existingRequest));

        AccreditationStatus latestStatus = new AccreditationStatus(existingRequest.getId(), AccreditationStatusType.valueOf(from), Instant.now());
        List<AccreditationStatus> statuses = new ArrayList<>();
        statuses.add(latestStatus);
        when(statusRepo.findByAccreditationId(existingRequest.getId())).thenReturn(statuses);

        AccreditationRequestStatusUpdateModel model = new AccreditationRequestStatusUpdateModel(existingRequest.getId(), AccreditationStatusType.valueOf(to));

        ServiceResult result = service.updateAccreditationRequestStatus(model);
        assertTrue(result.isSuccess());
    }

    @Test
    public void existingRequestRejectsNewOne() {
        String userId = "testUserId";

        AccreditationRequest existingRequest = new AccreditationRequest(userId, AccreditationRequestType.BY_INCOME, UUID.randomUUID(), Instant.now());
        List<AccreditationRequest> requests = new ArrayList<>();
        requests.add(existingRequest);
        when(requestRepo.findByUserId(userId)).thenReturn(requests);

        AccreditationStatus latestStatus = new AccreditationStatus(existingRequest.getId(), AccreditationStatusType.PENDING, Instant.now());
        List<AccreditationStatus> statuses = new ArrayList<>();
        statuses.add(latestStatus);
        when(statusRepo.findByAccreditationId(existingRequest.getId())).thenReturn(statuses);

        AccreditationRequestCreateModel model = new AccreditationRequestCreateModel();
        model.setAccreditationType(AccreditationRequestType.BY_NET_WORTH);
        model.setUserId(userId);
        model.setDocument(new DocumentCreateModel()); // stub

        ServiceResult result = service.registerAccreditationRequest(model);
        assertFalse(result.isSuccess());
        assertEquals(FailureReason.EXISTING_REQUEST, result.getFailureReason());
    }

    @Test
    public void creatingAccreditationRequest() {
        String userId = "testUserId";

        AccreditationRequest existingRequest = new AccreditationRequest(userId, AccreditationRequestType.BY_INCOME, UUID.randomUUID(), Instant.now());
        List<AccreditationRequest> requests = new ArrayList<>();
        requests.add(existingRequest);
        when(requestRepo.findByUserId(userId)).thenReturn(requests);

        AccreditationStatus latestStatus = new AccreditationStatus(existingRequest.getId(), AccreditationStatusType.PENDING, Instant.now());
        List<AccreditationStatus> statuses = new ArrayList<>();
        statuses.add(latestStatus);
        when(statusRepo.findByAccreditationId(existingRequest.getId())).thenReturn(statuses);

        AccreditationRequestCreateModel model = new AccreditationRequestCreateModel();
        model.setAccreditationType(AccreditationRequestType.BY_NET_WORTH);
        model.setUserId("testUserId2");
        model.setDocument(new DocumentCreateModel()); // stub

        ServiceResult result = service.registerAccreditationRequest(model);
        assertTrue(result.isSuccess());
    }

    @Test
    public void filteringConfirmedStatusesWorksCorrectly() {
        Instant now = Instant.now();

        List<AccreditationRequest> requests = new ArrayList<>();
        AccreditationRequest request1 = new AccreditationRequest("usertest1", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), now);
        AccreditationRequest request2 = new AccreditationRequest("usertest2", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), now);
        AccreditationRequest request3 = new AccreditationRequest("usertest3", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), now);
        AccreditationRequest request4 = new AccreditationRequest("usertest4", AccreditationRequestType.BY_INCOME, UUID.randomUUID(), now);

        requests.add(request1);
        requests.add(request2);
        requests.add(request3);
        requests.add(request4);
        
        when(requestRepo.getAll()).thenReturn(requests);

        List<AccreditationStatus> statuses1 = new ArrayList<>();
        List<AccreditationStatus> statuses2 = new ArrayList<>();
        List<AccreditationStatus> statuses3 = new ArrayList<>();
        List<AccreditationStatus> statuses4 = new ArrayList<>();

        statuses1.add(new AccreditationStatus(request1.getId(), AccreditationStatusType.CONFIRMED, now));
        statuses2.add(new AccreditationStatus(request2.getId(), AccreditationStatusType.PENDING, now));
        statuses3.add(new AccreditationStatus(request3.getId(), AccreditationStatusType.FAILED, now));
        statuses4.add(new AccreditationStatus(request4.getId(), AccreditationStatusType.EXPIRED, now));

        when(statusRepo.findByAccreditationId(request1.getId())).thenReturn(statuses1);
        when(statusRepo.findByAccreditationId(request2.getId())).thenReturn(statuses2);
        when(statusRepo.findByAccreditationId(request3.getId())).thenReturn(statuses3);
        when(statusRepo.findByAccreditationId(request4.getId())).thenReturn(statuses4);
        
        ServiceResult<List<AccreditationMetadataModel>> result = service.getAllConfirmedAccreditationMetadata();
        assertTrue(result.isSuccess());
        assertEquals(1, result.getValue().size());
    }
}

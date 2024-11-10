package com.james.ystechchallenge.data.impl;

import com.james.ystechchallenge.core.domain.AccreditationRequest;
import com.james.ystechchallenge.data.abstraction.AccreditationRequestRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InMemoryAccreditationRequestRepository implements AccreditationRequestRepository {

    private final Map<UUID, AccreditationRequest> memoryDb = new ConcurrentHashMap<>();
    
    @Override
    public void save(AccreditationRequest request) {
        memoryDb.put(request.getId(), request);
    }

    @Override
    public List<AccreditationRequest> findByUserId(String userId) {
        // we could improve this by building an index on userId -> requestId, but since this is all in memory, it should still be very fast
        return memoryDb
                .values()
                .stream()
                .filter(request -> request.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<AccreditationRequest> findById(UUID accreditationId) {
        return Optional.ofNullable(memoryDb.get(accreditationId));
    }

    @Override
    public List<AccreditationRequest> getAll() {
         return new ArrayList<>(memoryDb.values());
    }
}

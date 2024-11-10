package com.james.ystechchallenge.data.impl;

import com.james.ystechchallenge.core.domain.AccreditationStatus;
import com.james.ystechchallenge.data.abstraction.AccreditationStatusRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class InMemoryAccreditationStatusRepository implements AccreditationStatusRepository {

    private final Map<UUID, AccreditationStatus> memoryDb = new ConcurrentHashMap<>();

    @Override
    public void save(AccreditationStatus status) {
        memoryDb.put(status.getId(), status);
    }

    @Override
    public List<AccreditationStatus> findByAccreditationId(UUID accreditationId) {
        // we could improve this by building an index on accreditationId -> statusId, but since this is all in memory, it should still be very fast
        return memoryDb
                .values()
                .stream()
                .filter(status -> status.getAccreditationId().equals(accreditationId))
                .collect(Collectors.toList());
    }
}

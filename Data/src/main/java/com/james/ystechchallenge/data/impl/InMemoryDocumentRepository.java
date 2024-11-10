package com.james.ystechchallenge.data.impl;

import com.james.ystechchallenge.core.domain.Document;
import com.james.ystechchallenge.data.abstraction.DocumentRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class InMemoryDocumentRepository implements DocumentRepository {
    
    private final Map<UUID, Document> memoryDb = new ConcurrentHashMap<>();
    
    @Override
    public void save(Document document) {
        memoryDb.put(document.getId(), document);
    }
}

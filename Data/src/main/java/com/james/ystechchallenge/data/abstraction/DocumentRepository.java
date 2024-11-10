package com.james.ystechchallenge.data.abstraction;

import com.james.ystechchallenge.core.domain.Document;

/**
 * Represents a data source exposing operations on stored documents
 * Note that this is currently not used in any meaningful way
 * @author james
 */
public interface DocumentRepository {
    /**
     * Saves a document
     * @param document the document to be saved
     */
    void save(Document document);
}

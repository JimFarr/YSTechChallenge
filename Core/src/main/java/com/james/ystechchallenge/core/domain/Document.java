package com.james.ystechchallenge.core.domain;

import java.util.UUID;

/**
 * Domain model of a document
 * @author james
 */
public class Document {
    private final UUID id;
    private final String fileType;
    private final String content;
    
    public Document(String fileType, String content) {
        this.fileType = fileType;
        this.content = content;
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
            
    public String getContent() {
        return content;
    }
    
    public String getFileType() {
        return fileType;
    }
}

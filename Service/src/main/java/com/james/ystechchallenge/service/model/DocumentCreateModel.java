package com.james.ystechchallenge.service.model;

/**
 * Service model representing an intent to create a document
 * @author james
 */
public class DocumentCreateModel {
    private String name;
    private String fileType;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

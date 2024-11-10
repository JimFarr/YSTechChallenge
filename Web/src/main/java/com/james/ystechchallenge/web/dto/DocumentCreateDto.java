package com.james.ystechchallenge.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "An arbitrary document with metadata.")
public class DocumentCreateDto {

    @Schema(description = "Name of the document")
    @NotBlank(message = "'Name' field for document metadata is mandatory")
    @Size(max = 255, message = "Document name must be less than 255 characters")
    @JsonProperty("name")
    private String name;
    
    @Schema(description = "MIME type of the document", example = "application/pdf")
    @NotBlank(message = "'MIME type' is a mandatory field")
    @Size(max = 50, message = "MIME type must be less than 50 characters")
    @JsonProperty("mime_type")
    private String mimeType;
    
    @Schema(description = "The document content - encoded in Base64")
    @NotBlank(message = "Document content is mandatory")
    @JsonProperty("content")
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

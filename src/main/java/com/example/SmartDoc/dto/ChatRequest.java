package com.example.SmartDoc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatRequest {
    
    @NotBlank(message = "Message cannot be empty")
    private String message;
    
    /**
     * Optional: Filter retrieval to specific document
     * If null, searches across all documents
     */
    private Long documentId;
}
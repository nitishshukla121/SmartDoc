package com.example.SmartDoc.dto;

import lombok.Data;
 
@Data
public class ChatResponse {
    
    private String query;
    private String answer;
    private Long timestamp = System.currentTimeMillis();
}
 
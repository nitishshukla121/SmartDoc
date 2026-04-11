package com.example.SmartDoc.util;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PdfChunker {
    
    /**
     * Split text into chunks with overlap
     * 
     * @param text The full text to chunk
     * @param chunkSize Characters per chunk
     * @param overlap Characters to overlap between chunks
     * @return List of text chunks
     */
    public List<String> chunkText(String text, int chunkSize, int overlap) {
        List<String> chunks = new ArrayList<>();
        
        if (text == null || text.isEmpty()) {
            return chunks;
        }
        
        int start = 0;
        int textLength = text.length();
        
        while (start < textLength) {
            int end = Math.min(start + chunkSize, textLength);
            
            // Extract chunk
            String chunk = text.substring(start, end).trim();
            
            if (!chunk.isEmpty()) {
                chunks.add(chunk);
            }
            
            // Move to next chunk with overlap
            start += chunkSize - overlap;
        }
        
        return chunks;
    }
    
    /**
     * Clean text - remove excessive whitespace, special characters
     */
    public String cleanText(String text) {
        if (text == null) {
            return "";
        }
        
        return text
            .replaceAll("\\s+", " ")  // Multiple spaces → single space
            .replaceAll("[\\x00-\\x08\\x0B\\x0C\\x0E-\\x1F]", "")  // Remove control chars
            .trim();
    }
}
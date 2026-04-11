package com.example.SmartDoc.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.SmartDoc.dto.ChatRequest;
import com.example.SmartDoc.dto.ChatResponse;
import com.example.SmartDoc.service.ChatService;

import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class ChatController {
    
    private final ChatService chatService;
    
    /**
     * Send a chat message and get AI response
     * 
     * POST /api/chat
     */
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        try {
            log.info("Received chat request: {}", request.getMessage());
            
            String answer = chatService.chat(
                request.getMessage(),
                request.getDocumentId()
            );
            
            ChatResponse response = new ChatResponse();
            response.setAnswer(answer);
            response.setQuery(request.getMessage());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error processing chat", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * Stream chat response using Server-Sent Events (SSE)
     * 
     * POST /api/chat/stream
     * 
     * Note: Gemini doesn't support streaming in LangChain4j free tier,
     * so this simulates streaming by chunking the response
     */
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestBody ChatRequest request) {
        log.info("Received streaming chat request: {}", request.getMessage());
        
        try {
            // Get full response
            String answer = chatService.chat(
                request.getMessage(),
                request.getDocumentId()
            );
            
            // Simulate streaming by splitting into words
            String[] words = answer.split(" ");
            
            return Flux.fromArray(words)
                .delayElements(Duration.ofMillis(50))  // 50ms delay between words
                .map(word -> word + " ");
                
        } catch (Exception e) {
            log.error("Error in streaming chat", e);
            return Flux.just("Error: " + e.getMessage());
        }
    }
}
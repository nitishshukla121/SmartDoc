package com.example.SmartDoc.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartDoc.Entity.DocumentMetadata;
import com.example.SmartDoc.service.DocumentIngestionService;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class DocumentController {
    
    private final DocumentIngestionService documentIngestionService;
    
    /**
     * Upload and process a PDF document
     * 
     * POST /api/documents/upload
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received upload request for: {}", file.getOriginalFilename());
            
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }
            
            if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are allowed");
            }
            
            // Process document
            DocumentMetadata metadata = documentIngestionService.ingestDocument(file);
            
            return ResponseEntity.ok(metadata);
            
        } catch (Exception e) {
            log.error("Error uploading document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error processing document: " + e.getMessage());
        }
    }
    
    /**
     * Get all uploaded documents
     * 
     * GET /api/documents
     */
    @GetMapping
    public ResponseEntity<List<DocumentMetadata>> getAllDocuments() {
        List<DocumentMetadata> documents = documentIngestionService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }
    
    /**
     * Delete a document
     * 
     * DELETE /api/documents/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        try {
            documentIngestionService.deleteDocument(id);
            return ResponseEntity.ok("Document deleted successfully");
        } catch (Exception e) {
            log.error("Error deleting document", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error deleting document: " + e.getMessage());
        }
    }
}
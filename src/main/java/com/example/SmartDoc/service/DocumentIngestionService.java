package com.example.SmartDoc.service;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.SmartDoc.Entity.DocumentMetadata;
import com.example.SmartDoc.config.AppConfig;
import com.example.SmartDoc.repository.DocumentRepository;
import com.example.SmartDoc.util.PdfChunker;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentIngestionService {
    
    private final DocumentRepository documentRepository;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final PdfChunker pdfChunker;
    private final AppConfig appConfig;
    
    /**
     * Process uploaded PDF: save file, parse, chunk, embed, store
     */
    public DocumentMetadata ingestDocument(MultipartFile file) throws IOException {
        log.info("Starting ingestion for file: {}", file.getOriginalFilename());
        
        // 1. Save file to disk
        String savedFilePath = saveFile(file);
        log.info("File saved to: {}", savedFilePath);
        
        // 2. Parse PDF to extract text
        String fullText = parsePdf(savedFilePath);
        log.info("Extracted {} characters from PDF", fullText.length());
        
        // 3. Clean and chunk text
        String cleanedText = pdfChunker.cleanText(fullText);
        List<String> chunks = pdfChunker.chunkText(
            cleanedText, 
            appConfig.getChunkSize(), 
            appConfig.getChunkOverlap()
        );
        log.info("Created {} chunks", chunks.size());
        
        // 4. Save document metadata
        DocumentMetadata metadata = new DocumentMetadata();
        metadata.setFileName(file.getOriginalFilename());
        metadata.setFilePath(savedFilePath);
        metadata.setFileSize(file.getSize());
        metadata.setUploadedAt(LocalDateTime.now());
        metadata.setChunkCount(chunks.size());
        metadata.setProcessed(true);
        metadata = documentRepository.save(metadata);
        log.info("Saved document metadata with ID: {}", metadata.getId());
        
        // 5. Embed chunks and store in PGVector
        embedAndStoreChunks(chunks, metadata.getId());
        log.info("Embeddings stored successfully");
        
        return metadata;
    }
    
    /**
     * Save uploaded file to upload directory
     */
    private String saveFile(MultipartFile file) throws IOException {
        // Create upload directory if it doesn't exist
        Path uploadDir = Paths.get(appConfig.getUploadDir());
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String filename = timestamp + "_" + originalFilename;
        
        // Save file
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return filePath.toString();
    }
    
    /**
     * Parse PDF and extract text
     */
    private String parsePdf(String filePath) throws IOException {
        DocumentParser parser = new ApachePdfBoxDocumentParser();
        
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            Document document = parser.parse(inputStream);
            return document.text();
        }
    }
    
    /**
     * Generate embeddings for chunks and store in PGVector
     */
    private void embedAndStoreChunks(List<String> chunks, Long documentId) {
        List<TextSegment> segments = new ArrayList<>();
        
        for (int i = 0; i < chunks.size(); i++) {
            String chunkText = chunks.get(i);
            
            // Create metadata for this chunk using LangChain4j Metadata class
            dev.langchain4j.data.document.Metadata metadata = dev.langchain4j.data.document.Metadata.from(
                "documentId", documentId.toString()
            );
            
            // Create text segment with metadata
            TextSegment segment = TextSegment.from(chunkText, metadata);
            segments.add(segment);
        }
        
        // Generate embeddings for all segments (batch processing)
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        
        // Store in PGVector
        embeddingStore.addAll(embeddings, segments);
        
        log.info("Stored {} embeddings in PGVector", embeddings.size());
    }
    
    /**
     * Get all uploaded documents
     */
    public List<DocumentMetadata> getAllDocuments() {
        return documentRepository.findAll();
    }
    
    /**
     * Delete document and its embeddings
     */
    public void deleteDocument(Long documentId) {
        // TODO: Remove embeddings from PGVector (requires custom query)
        documentRepository.deleteById(documentId);
        log.info("Deleted document: {}", documentId);
    }
}
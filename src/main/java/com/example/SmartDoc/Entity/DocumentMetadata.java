package com.example.SmartDoc.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "document_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fileName;
    
    @Column(nullable = false)
    private String filePath;
    
    private Long fileSize;
    
    @Column(nullable = false)
    private LocalDateTime uploadedAt;
    
    private Integer chunkCount;
    
    private Boolean processed = false;
    
    @Column(length = 1000)
    private String description;
}
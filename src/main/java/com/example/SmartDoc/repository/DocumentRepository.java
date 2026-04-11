package com.example.SmartDoc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SmartDoc.Entity.DocumentMetadata;

import java.util.List;
 
@Repository
public interface DocumentRepository extends JpaRepository<DocumentMetadata, Long> {
    
    /**
     * Find documents by filename
     */
    List<DocumentMetadata> findByFileName(String fileName);
    
    /**
     * Find all processed documents
     */
    List<DocumentMetadata> findByProcessedTrue();
    
    /**
     * Find all unprocessed documents
     */
    List<DocumentMetadata> findByProcessedFalse();
}
 
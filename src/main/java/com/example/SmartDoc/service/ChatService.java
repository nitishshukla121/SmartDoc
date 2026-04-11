package com.example.SmartDoc.service;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.SmartDoc.config.AppConfig;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    
    private final ChatLanguageModel chatModel;
    private final EmbeddingModel embeddingModel;
    private final EmbeddingStore<TextSegment> embeddingStore;
    private final AppConfig appConfig;
    
    /**
     * Answer user query using RAG (Retrieval-Augmented Generation)
     * 
     * @param query User's question
     * @param documentId Optional: filter by specific document
     * @return AI-generated answer based on retrieved context
     */
    public String chat(String query, Long documentId) {
        log.info("Processing query: {}", query);
        
        // 1. Embed the user query
        Embedding queryEmbedding = embeddingModel.embed(query).content();
        
        // 2. Retrieve most relevant chunks from PGVector
        List<EmbeddingMatch<TextSegment>> relevantChunks = embeddingStore.findRelevant(
            queryEmbedding,
            appConfig.getTopKRetrieval()
        );
        
        log.info("Retrieved {} relevant chunks", relevantChunks.size());
        
        // 3. Filter by documentId if specified
        if (documentId != null) {
            relevantChunks = relevantChunks.stream()
                .filter(match -> {
                    String docId = match.embedded().metadata("documentId");
                    return docId != null && docId.equals(documentId.toString());
                })
                .collect(Collectors.toList());
            
            log.info("Filtered to {} chunks from document {}", relevantChunks.size(), documentId);
        }
        
        // 4. Build context from retrieved chunks
        String context = buildContext(relevantChunks);
        
        // 5. Create prompt with context
        String prompt = buildPrompt(query, context);
        
        // 6. Send to Gemini and get response
        Response<AiMessage> response = chatModel.generate(UserMessage.from(prompt));
        String answer = response.content().text();
        
        log.info("Generated answer: {}", answer.substring(0, Math.min(100, answer.length())));
        
        return answer;
    }
    
    /**
     * Build context string from retrieved chunks
     */
    private String buildContext(List<EmbeddingMatch<TextSegment>> matches) {
        if (matches.isEmpty()) {
            return "No relevant context found in the documents.";
        }
        
        StringBuilder context = new StringBuilder();
        context.append("Relevant information from the documents:\n\n");
        
        for (int i = 0; i < matches.size(); i++) {
            TextSegment segment = matches.get(i).embedded();
            context.append("--- Excerpt ").append(i + 1).append(" ---\n");
            context.append(segment.text()).append("\n\n");
        }
        
        return context.toString();
    }
    
    /**
     * Build the final prompt with context and query
     */
    private String buildPrompt(String query, String context) {
        return """
            You are a helpful AI assistant that answers questions based on the provided context from documents.
            
            %s
            
            User Question: %s
            
            Instructions:
            - Answer the question based ONLY on the context provided above
            - If the answer is not in the context, say "I don't have enough information in the documents to answer that question."
            - Be concise and accurate
            - Cite relevant parts of the context when appropriate
            
            Answer:
            """.formatted(context, query);
    }
    
    /**
     * Simple chat without RAG (just direct Gemini response)
     */
    public String simpleChat(String message) {
        Response<AiMessage> response = chatModel.generate(UserMessage.from(message));
        return response.content().text();
    }
}
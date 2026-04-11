package com.example.SmartDoc.config;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PGVectorConfig {

	@Value("${spring.datasource.url}")
	private String jdbcUrl;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	/**
	 * PGVector Embedding Store - stores document embeddings
	 */
	@Bean
	public EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel) {
		return PgVectorEmbeddingStore.builder().host("localhost").port(5432).database("smartdoc").user(username)
				.password(password).table("document_embeddings").dimension(384) // all-MiniLM-L6-v2 produces 384-dim
																				// vectors
				.createTable(true).dropTableFirst(false).build();
	}
}

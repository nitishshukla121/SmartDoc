package com.example.SmartDoc.config;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LangChainConfig {

	@Value("${langchain4j.gemini.api-key}")
	private String geminiApiKey;

	@Value("${langchain4j.gemini.model-name}")
	private String modelName;

	@Value("${langchain4j.gemini.temperature}")
	private Double temperature;

	@Value("${langchain4j.gemini.max-output-tokens}")
	private Integer maxTokens;

	/**
	 * Gemini Chat Model - for generating responses
	 */
	@Bean
	public ChatLanguageModel chatLanguageModel() {
		return GoogleAiGeminiChatModel.builder().apiKey(geminiApiKey).modelName(modelName).temperature(temperature)
				.maxOutputTokens(maxTokens).build();
	}

	/**
	 * Local Embedding Model - runs on your machine, no API needed! Uses
	 * all-MiniLM-L6-v2 (384 dimensions, fast, free)
	 */
	@Bean
	public EmbeddingModel embeddingModel() {
		return new AllMiniLmL6V2EmbeddingModel();
	}
}
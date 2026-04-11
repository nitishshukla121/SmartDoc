package com.example.SmartDoc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

	private String uploadDir = "./uploads";
	private int chunkSize = 800;
	private int chunkOverlap = 150;
	private int topKRetrieval = 5;
	private int maxHistoryMessages = 10;

	// Getters and setters
	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public int getChunkOverlap() {
		return chunkOverlap;
	}

	public void setChunkOverlap(int chunkOverlap) {
		this.chunkOverlap = chunkOverlap;
	}

	public int getTopKRetrieval() {
		return topKRetrieval;
	}

	public void setTopKRetrieval(int topKRetrieval) {
		this.topKRetrieval = topKRetrieval;
	}

	public int getMaxHistoryMessages() {
		return maxHistoryMessages;
	}

	public void setMaxHistoryMessages(int maxHistoryMessages) {
		this.maxHistoryMessages = maxHistoryMessages;
	}
}
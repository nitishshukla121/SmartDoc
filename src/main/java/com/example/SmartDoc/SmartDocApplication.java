package com.example.SmartDoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
@EnableAsync        // needed for CompletableFuture concurrent processing
@EnableScheduling 
@SpringBootApplication
public class SmartDocApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartDocApplication.class, args);
	}

}

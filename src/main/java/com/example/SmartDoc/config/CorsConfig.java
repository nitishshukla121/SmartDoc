package com.example.SmartDoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();

		// Allow React frontend origins
		config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:5173", // Vite default port
				"http://127.0.0.1:3000", "http://127.0.0.1:5173"));

		// Allow credentials (cookies, auth headers)
		config.setAllowCredentials(true);

		// Allow all HTTP methods
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

		// Allow all headers
		config.setAllowedHeaders(List.of("*"));

		// Expose headers for client access
		config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Total-Count"));

		// Cache preflight requests for 1 hour
		config.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return new CorsFilter(source);
	}
}
package com.nnh.ra1neestore.Config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to load environment variables from .env file
 * This runs before Spring Boot loads application.properties
 */
@Configuration
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .directory("./") // Look for .env in project root
                    .ignoreIfMissing() // Don't fail if .env is missing
                    .load();

            // Set environment variables for Spring Boot to use
            dotenv.entries().forEach(entry -> {
                System.setProperty(entry.getKey(), entry.getValue());
            });

            System.out.println("✅ Loaded environment variables from .env file");
        } catch (Exception e) {
            System.err.println("⚠️ Warning: Could not load .env file - " + e.getMessage());
        }
    }
}

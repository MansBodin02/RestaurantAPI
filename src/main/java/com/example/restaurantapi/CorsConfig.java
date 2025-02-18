package com.example.restaurantapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Gäller för alla endpoints
                        .allowedOrigins("http://localhost:3000") // Anpassa efter din frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Tillåtna HTTP-metoder
                        .allowedHeaders("*") // Tillåt alla headers
                        .allowCredentials(true); // Tillåt cookies och autentisering
            }
        };
    }
}

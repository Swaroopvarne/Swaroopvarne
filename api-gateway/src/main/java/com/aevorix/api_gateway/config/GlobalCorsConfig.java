package com.aevorix.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Allow any origin pattern (use only in dev! In prod, put exact domain like "https://myapp.com")
        config.addAllowedOriginPattern("*");

        // Allow standard HTTP methods
        config.addAllowedMethod("*"); // Or specify: "GET", "POST", "PUT", "DELETE", etc.

        // Allow any header
        config.addAllowedHeader("*");

        // If you need to send cookies / Authorization header from browser JS
        config.setAllowCredentials(true);

        // How long the browser should cache the preflight response (optional)
        config.setMaxAge(3600L); // seconds

        // Register config for all routes
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}

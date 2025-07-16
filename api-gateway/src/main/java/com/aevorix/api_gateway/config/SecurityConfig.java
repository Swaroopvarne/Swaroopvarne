package com.aevorix.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
            		.pathMatchers(
            			    "/api-gateway/user-auth/api/public/auth/registerUser",
            			    "/api-gateway/user-auth/api/public/auth/login",
            			    "/api-gateway/swagger-ui.html",
            			    "/api-gateway/swagger-ui/**",
            			    "/api-gateway/v3/api-docs",
            			    "/api-gateway/swagger-resources/**",
            			    "/api-gateway/webjars/**"
            			).permitAll()
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

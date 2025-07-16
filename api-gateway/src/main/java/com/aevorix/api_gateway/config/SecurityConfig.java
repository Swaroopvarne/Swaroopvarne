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
            			    "/api-gateway/user-auth/api/auth/registerUser",
            			    "/api-gateway/user-auth/api/auth/login",
            			    "/api-gateway/user-auth/swagger-ui.html",
            			    "/api-gateway/user-auth/swagger-ui/**",
            			    "/api-gateway/user-auth/v3/api-docs",
            			    "/api-gateway/user-auth/swagger-resources/**",
            			    "/api-gateway/user-auth/webjars/**"
            			).permitAll()
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}

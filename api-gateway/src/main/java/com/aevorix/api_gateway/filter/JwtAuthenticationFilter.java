package com.aevorix.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String path = exchange.getRequest().getURI().getPath();
		System.out.println("==> Request path: " + path); // DEBUG

		// ✅ Allow these paths without JWT
		if (isPublicPath(path)) {
			return chain.filter(exchange);
		}

		// 🔐 JWT Token validation
		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		String token = authHeader.substring(7);
		try {
			// Parse token & validate
			Claims claims = Jwts.parser().setSigningKey(jwtSecret.getBytes()).parseClaimsJws(token).getBody();
			// You can set claims into request attribute / context if needed
		} catch (Exception e) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}

		return chain.filter(exchange);
	}

	/**
	 * Matches paths which should be public (no JWT required)
	 */
	private boolean isPublicPath(String path) {
		return path != null && (path.equals("/api-gateway/user-auth/api/auth/login")
				|| path.equals("/api-gateway/user-auth/api/auth/registerUser")
				|| path.equals("/api-gateway/user-auth/api/auth/verifyRegister")
				|| path.startsWith("/api-gateway/user-auth/swagger-ui")
				|| path.equals("/api-gateway/user-auth/swagger-ui.html")
				|| path.equals("/api-gateway/user-auth/v3/api-docs")
				|| path.startsWith("/api-gateway/user-auth/swagger-resources")
				|| path.startsWith("/api-gateway/user-auth/webjars"));
	}

	@Override
	public int getOrder() {
		return -1; // High priority
	}
}

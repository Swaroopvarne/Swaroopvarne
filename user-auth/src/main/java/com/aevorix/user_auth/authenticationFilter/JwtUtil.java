package com.aevorix.user_auth.authenticationFilter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private final JwtProperties jwtProperties;

	public JwtUtil(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername())
				.claim("role", userDetails.getAuthorities().toString())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs())) // 1 day
				.signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)),
						SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8))).build()
				.parseClaimsJws(token).getBody().getSubject();
	}

	public boolean isTokenValid(String token, UserDetails userDetails) {
		return extractUsername(token).equals(userDetails.getUsername());
	}
}

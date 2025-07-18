package com.aevorix.user_service.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.aevorix.common_service.config.JwtProperties;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
	
	private final JwtProperties jwtProperties;

	public JwtUtil(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}
	
//    public String generateToken(UserDetails userDetails) {
//        return Jwts.builder()
//            .setSubject(userDetails.getUsername())
//            .setIssuedAt(new Date())
//            .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs())) // 1 day
//            .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes()), SignatureAlgorithm.HS256)
//            .compact();
//    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(jwtProperties.getSecret().getBytes())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        return extractUsername(token).equals(userDetails.getUsername());
    }
}

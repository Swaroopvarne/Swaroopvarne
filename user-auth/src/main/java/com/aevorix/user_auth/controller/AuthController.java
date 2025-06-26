package com.aevorix.user_auth.controller;

import java.util.List;
import java.util.Map;

import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aevorix.user_auth.AuthRequest.AuthRequest;
import com.aevorix.user_auth.authenticationFilter.JwtUtil;
import com.aevorix.user_auth.repo.UserRepository;
import com.aevorix.user_auth.utility.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return ResponseEntity.badRequest().body("User email already exists");
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");
		user.setUsername(user.getUsername());
		user.setMobileNumber(user.getMobileNumber());
		user.setStatus("A");
		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		try {
			String token = jwtUtil.generateToken(new org.springframework.security.core.userdetails.User(user.getEmail(),
					user.getPassword(), List.of(new SimpleGrantedAuthority(user.getRole()))));

			return ResponseEntity.ok(Map.of("token", token));
		} catch (Exception e) {
			e.printStackTrace(); // Log the actual cause
			return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR)
					.body("Token generation failed: " + e.getMessage());
		}
	}
}

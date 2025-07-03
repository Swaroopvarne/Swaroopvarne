package com.aevorix.user_auth.controller;

import java.time.LocalDateTime;
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

import com.aevorix.common_service.apiResponse.BaseResponse;
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
	public ResponseEntity<BaseResponse<String>> register(@RequestBody User user) {
	    try {
	        // Validate input fields
	        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(new BaseResponse<>(400, "Email is required", null, LocalDateTime.now()));
	        }
	        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(new BaseResponse<>(400, "Username is required", null, LocalDateTime.now()));
	        }
	        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(new BaseResponse<>(400, "Password is required", null, LocalDateTime.now()));
	        }
	        if (user.getMobileNumber() == null || user.getMobileNumber().trim().isEmpty()) {
	            return ResponseEntity.badRequest().body(new BaseResponse<>(400, "Mobile number is required", null, LocalDateTime.now()));
	        }

	        // Check if email already exists
	        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
	            return ResponseEntity.badRequest().body(new BaseResponse<>(409, "User with this email already exists", null, LocalDateTime.now()));
	        }

	        // Prepare and save user
	        user.setPassword(passwordEncoder.encode(user.getPassword()));
	        user.setRole(user.getRole());
	        user.setStatus("A");
	        user.setCreatedAt(LocalDateTime.now());

	        userRepository.save(user);

	        return ResponseEntity.ok(new BaseResponse<>(200, "User registered successfully", "Success", LocalDateTime.now()));

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(500).body(
	                new BaseResponse<>(500, "An unexpected error occurred", e.getMessage(), LocalDateTime.now())
	        );
	    }
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

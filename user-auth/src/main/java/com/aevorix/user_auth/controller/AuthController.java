package com.aevorix.user_auth.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aevorix.common_service.apiResponse.BaseResponse;
import com.aevorix.common_service.config.JwtUtil;
import com.aevorix.user_auth.AuthRequest.AuthRequest;
import com.aevorix.user_auth.model.RegisterRequest;
import com.aevorix.user_auth.model.User;
import com.aevorix.user_auth.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

	@PostMapping("/registerUser")
	public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
		if (userRepository.findByEmail(req.getEmail()).isPresent()) {
			return ResponseEntity.ok().body(BaseResponse.error(400, "Email already registered"));
		}
		if (userRepository.findByMobileNumber(req.getMobileNo()).isPresent()) {
			return ResponseEntity.ok().body(BaseResponse.error(400, "Mobile number already registered"));
		}

		String roleCode = req.getRole() != null ? req.getRole().toUpperCase() : "C";
		String role;

		switch (roleCode) {
		case "A":
			return ResponseEntity.ok().body(BaseResponse.error(400, "Cannot self-register as ADMIN"));
		case "E":
			role = "ROLE_ENGINEER";
			break;
		case "C":
		default:
			role = "ROLE_CUSTOMER";
			break;
		}

		User user = new User();
		user.setUsername(req.getUsername());
		user.setEmail(req.getEmail());
		user.setMobileNumber(req.getMobileNo());
		user.setPassword(passwordEncoder.encode(req.getPassword()));
		user.setRole(role);
		user.setStatus("A");

		userRepository.save(user);

		return ResponseEntity.ok(BaseResponse.success("User registered successfully", "Success"));
	}

	@PostMapping("/login")
	public ResponseEntity<BaseResponse<?>> login(@RequestBody AuthRequest request) {
		String loginField = request.getEmail() != null ? request.getEmail() : request.getMobileNo();
		LocalDateTime now = LocalDateTime.now();

		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginField, request.getPassword()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new BaseResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials", null, now));
		}

		try {
			User user = userRepository.findByEmail(request.getEmail())
					.orElseGet(() -> userRepository.findByMobileNumber(request.getMobileNo()).orElseThrow(
							() -> new UsernameNotFoundException("User not found with email/mobile: " + loginField)));

			UserDetails userDetails = org.springframework.security.core.userdetails.User
					.withUsername(user.getEmail() != null ? user.getEmail() : user.getMobileNumber())
					.password(user.getPassword()).authorities(new SimpleGrantedAuthority(user.getRole())).build();

			String token = jwtUtil.generateToken(userDetails);

			return ResponseEntity
					.ok(new BaseResponse<>(HttpStatus.OK.value(), "Login successful", Map.of("token", token), now));
		} catch (UsernameNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null, now));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(
					HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong: " + e.getMessage(), null, now));
		}
	}

}

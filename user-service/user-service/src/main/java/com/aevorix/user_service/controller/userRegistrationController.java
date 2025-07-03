package com.aevorix.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aevorix.user_service.dto.loginRequest;
import com.aevorix.user_service.dto.registrationRequest;
import com.aevorix.user_service.dto.registrationVerifyRequest;
import com.aevorix.user_service.service.userRegistrationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class userRegistrationController {

	private final userRegistrationService registrationService;

	@PostMapping("/registerUser")
	public ResponseEntity<?> register(@RequestBody registrationRequest request) {
		return registrationService.registerUser(request.getMobileNumber(), request.getName());
	}

	@PostMapping("/verifyRegister")
	public ResponseEntity<?> verify(@RequestBody registrationVerifyRequest request) {
		return registrationService.verifyRegister(request.getMobileNumber(), request.getOtp(), request.getName());
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody loginRequest req) {
		return registrationService.userlogin(req.getMobileNumber(), req.getName());
	}

//	// Get all users
//	@GetMapping
//	public ResponseEntity<List<UserDTO>> getAllUsers() {
//		List<User> users = userRepository.findAll();
//		List<UserDTO> dtos = users.stream().map(this::convertToDto).collect(Collectors.toList());
//		return ResponseEntity.ok(dtos);
//	}
//
//	// Get user by ID
//	@GetMapping("/{id}")
//	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
//		User user = userRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//		return ResponseEntity.ok(convertToDto(user));
//	}
//
//	// Create user
//	@PostMapping(Urls.CREATE_USER)
//	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
//		User saved = userRepository.save(convertToEntity(dto));
//		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
//	}
//
//	// Update user
//	@PutMapping("/{id}")
//	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
//		User existing = userRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//		existing.setUsername(dto.getUsername());
//		existing.setEmail(dto.getEmail());
//		existing.setMobileNumber(dto.getMobileNumber());
//		User updated = userRepository.save(existing);
//		return ResponseEntity.ok(convertToDto(updated));
//	}
//
//	// Delete user
//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//		User existing = userRepository.findById(id)
//				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
//		userRepository.delete(existing);
//		return ResponseEntity.noContent().build();
//	}
//
//	// Mapper methods
//	private UserDTO convertToDto(User user) {
//		return modelMapper.map(user, UserDTO.class);
//	}
//
//	private User convertToEntity(UserDTO dto) {
//		return modelMapper.map(dto, User.class);
//	}
//
//	@GetMapping("/items")
//	public ResponseEntity<List<String>> getAllItems() {
//		return ResponseEntity.ok(List.of("Shirt", "Phone", "Shoes"));
//	}
//
//	@PostMapping("/checkout")
//	public ResponseEntity<String> checkout() {
//		return ResponseEntity.ok("Checkout successful!");
//	}
//	
//	@GetMapping("/user/secure")
//    public String securedHello() {
//        return "Hello, this is a secured user endpoint!";
//    }
//
//    @GetMapping("/user/public/hello")
//    public String publicHello() {
//        return "Hello, this is a public user endpoint!";
//    }
}

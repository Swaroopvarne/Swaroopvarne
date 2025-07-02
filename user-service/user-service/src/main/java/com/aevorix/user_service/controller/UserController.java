package com.aevorix.user_service.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aevorix.user_service.dto.CartItemDTO;
import com.aevorix.user_service.dto.UserDTO;
import com.aevorix.user_service.entity.User;
import com.aevorix.user_service.exception.ResourceNotFoundException;
import com.aevorix.user_service.repo.UserRepository;
import com.aevorix.user_service.utils.Urls;

@RestController
//@RequestMapping(Urls.BASEURL_USER)
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	// Get all users
	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<User> users = userRepository.findAll();
		List<UserDTO> dtos = users.stream().map(this::convertToDto).collect(Collectors.toList());
		return ResponseEntity.ok(dtos);
	}

	// Get user by ID
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		return ResponseEntity.ok(convertToDto(user));
	}

	// Create user
	@PostMapping(Urls.CREATE_USER)
	public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO dto) {
		User saved = userRepository.save(convertToEntity(dto));
		return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(saved));
	}

	// Update user
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
		User existing = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		existing.setUsername(dto.getUsername());
		existing.setEmail(dto.getEmail());
		existing.setMobileNumber(dto.getMobileNumber());
		User updated = userRepository.save(existing);
		return ResponseEntity.ok(convertToDto(updated));
	}

	// Delete user
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		User existing = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
		userRepository.delete(existing);
		return ResponseEntity.noContent().build();
	}

	// Mapper methods
	private UserDTO convertToDto(User user) {
		return modelMapper.map(user, UserDTO.class);
	}

	private User convertToEntity(UserDTO dto) {
		return modelMapper.map(dto, User.class);
	}

	@GetMapping("/items")
	public ResponseEntity<List<String>> getAllItems() {
		return ResponseEntity.ok(List.of("Shirt", "Phone", "Shoes"));
	}

	@PostMapping("/checkout")
	public ResponseEntity<String> checkout() {
		return ResponseEntity.ok("Checkout successful!");
	}
	
	@GetMapping("/user/secure")
    public String securedHello() {
        return "Hello, this is a secured user endpoint!";
    }

    @GetMapping("/user/public/hello")
    public String publicHello() {
        return "Hello, this is a public user endpoint!";
    }
}

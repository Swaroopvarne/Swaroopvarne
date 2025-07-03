package com.aevorix.user_service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.aevorix.common_service.apiResponse.BaseResponse;
import com.aevorix.common_service.commonUtils.commonMethods;
import com.aevorix.user_service.dto.UserDTO;
import com.aevorix.user_service.entity.User;
import com.aevorix.user_service.repo.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class userRegistrationService {

	private final ModelMapper modelMapper;

	@Autowired
	public UserRepository userRepository;

//	@Value("${jwt.secret}")
//	private String jwtSecret;

	public ResponseEntity<?> registerUser(String mobileNumber, String name) {

		if (mobileNumber == null || mobileNumber.trim().isEmpty()) {
			return ResponseEntity.badRequest().body("Mobile number is required");
		}

		String formattedMobile = "+91" + mobileNumber.trim();

		// Check if user already exists
		if (userRepository.findByMobileNumber(formattedMobile).isPresent()) {
			return ResponseEntity.ok().body("User with this mobile number already exists");
		}

		// Generate 6-digit OTP
		int otp = commonMethods.genOtp(6);

		// Create UserDTO
		UserDTO userDto = new UserDTO();
		userDto.setMobileNumber(formattedMobile);
		userDto.setOtp(otp);
		userDto.setOtpGenerated(LocalDateTime.now());
		userDto.setStatus("P");
		userDto.setRole("ROLE_USER");
		userDto.setUsername(name != null ? name : "user_" + mobileNumber);

		// Map DTO to entity
		User user = modelMapper.map(userDto, User.class);

		// Save user
		userRepository.save(user);

		// Simulate OTP sending (Replace with actual SMS service)
		System.out.println("OTP sent to " + formattedMobile + ": " + otp);

		return ResponseEntity.ok("OTP sent successfully to " + mobileNumber);
	}

	public ResponseEntity<?> verifyRegister(String mobileNumber, int otp, String name) {
		String formattedMobile = "+91" + mobileNumber.trim();

		Optional<User> optionalUser = userRepository.findByMobileNumber(formattedMobile);
		// OTP validation
		if (optionalUser.get().getOtp() != otp) {
			return ResponseEntity.ok().body("Invalid OTP");
		}

		// Check if user exists with this mobile and status P (pending)
		if (optionalUser.isEmpty()) {
			return ResponseEntity.badRequest().body("User not found. Please register first.");
		}

		User user = optionalUser.get();

		// Update user fields
		user.setUsername(name != null ? name : "user_" + mobileNumber); // Avoid null username
		user.setStatus("A");
		user.setOtp(0); // clear OTP
		user.setOtpGeneratedAt(null);
		user.setPassword(""); // or leave null if unused

		userRepository.save(user);

		Map<String, Object> response = new HashMap<>();
		response.put("message", "Registration successful, welcome!");
		response.put("status", HttpStatus.SC_OK);
		response.put("response", "Done");

		return ResponseEntity.ok(response);

	}

	public ResponseEntity<?> userlogin(String mobNum, String name) {
		if (mobNum == null || mobNum.trim().isEmpty() || mobNum.length() == 10) {
			return ResponseEntity.badRequest().body("Mobile number is required");
		}

		String formattedMobile = "+91" + mobNum.trim();

		// Check if user already exists
		if (userRepository.findByMobileNumber(formattedMobile).isPresent()) {
			int otp = commonMethods.genOtp(6);
		}

		return ResponseEntity
				.ok(new BaseResponse<>(200, "User registered successfully", "Success", LocalDateTime.now()));

	}
}

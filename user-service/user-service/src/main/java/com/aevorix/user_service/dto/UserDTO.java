package com.aevorix.user_service.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDTO {

	private String username;
	private String password;
	private String email;
	private int otp;
	private String role;
	private LocalDateTime otpGenerated;
	private String status;
	private String mobileNumber;
	private LocalDateTime createdAt= LocalDateTime.now();

}

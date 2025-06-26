package com.aevorix.user_service.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

	private String username;
	private String password;
	private String email;
	private String status;
	private String mobileNumber;
	private LocalDateTime createdAt= LocalDateTime.now();

}

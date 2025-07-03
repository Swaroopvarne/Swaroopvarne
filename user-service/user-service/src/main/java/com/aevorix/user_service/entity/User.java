package com.aevorix.user_service.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String username;

	@Column
	private String password;

	@Column
	private String email;

	@Column
	private int otp;

	private LocalDateTime otpGeneratedAt;

	@Column(name = "mobile_Number", nullable = false, unique = true, length = 13)
	private String mobileNumber;

	@Column(name = "role")
	private String role;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "status")
	private String status;
}
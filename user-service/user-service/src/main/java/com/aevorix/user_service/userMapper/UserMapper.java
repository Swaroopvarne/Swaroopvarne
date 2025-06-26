package com.aevorix.user_service.userMapper;

import com.aevorix.user_service.dto.UserDTO;
import com.aevorix.user_service.entity.User;

public class UserMapper {

	public static UserDTO toDTO(User user) {
		UserDTO dto = new UserDTO();
		dto.setUsername(user.getUsername());
		dto.setEmail(user.getEmail());
		dto.setStatus(user.getStatus());
		return dto;
	}

	public static User toEntity(UserDTO dto) {
		User user = new User();
		user.setUsername(dto.getUsername());
		user.setEmail(dto.getEmail());
		user.setStatus(dto.getStatus());
		return user;
	}
}

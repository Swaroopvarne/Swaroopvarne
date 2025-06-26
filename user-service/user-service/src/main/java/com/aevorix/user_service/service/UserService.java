package com.aevorix.user_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.aevorix.user_service.dto.UserDTO;

@Service
public interface UserService {

	UserDTO getUserById(Long id);

	List<UserDTO> getAllUsers();

	UserDTO createUser(UserDTO userDTO);

	UserDTO updateUser(Long id, UserDTO userDTO);

	void deleteUser(Long id);
}

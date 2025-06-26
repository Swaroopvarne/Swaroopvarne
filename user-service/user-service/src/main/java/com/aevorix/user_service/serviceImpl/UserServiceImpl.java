package com.aevorix.user_service.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aevorix.user_service.dto.UserDTO;
import com.aevorix.user_service.entity.User;
import com.aevorix.user_service.exception.ResourceNotFoundException;
import com.aevorix.user_service.repo.UserRepository;
import com.aevorix.user_service.service.UserService;
import com.aevorix.user_service.userMapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.toEntity(userDTO);
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus("ACTIVE");
        User saved = userRepository.save(user);
        return UserMapper.toDTO(saved);
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existing.setUsername(userDTO.getUsername());
        existing.setEmail(userDTO.getEmail());
        existing.setStatus(userDTO.getStatus());

        User updated = userRepository.save(existing);
        return UserMapper.toDTO(updated);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}

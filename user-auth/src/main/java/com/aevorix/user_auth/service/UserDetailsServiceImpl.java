package com.aevorix.user_auth.service;

import com.aevorix.user_auth.model.User;
import com.aevorix.user_auth.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrMobile) throws UsernameNotFoundException {
        return userRepository.findByEmail(usernameOrMobile)
                .map(user -> toUserDetails(user))
                .orElseGet(() -> userRepository.findByMobileNumber(usernameOrMobile)
                        .map(this::toUserDetails)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email or mobile: " + usernameOrMobile)));
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail() != null ? user.getEmail() : user.getMobileNumber())
                .password(user.getPassword())
                .authorities(user.getRole())
                .build();
    }
}

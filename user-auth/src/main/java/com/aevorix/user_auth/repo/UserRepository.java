package com.aevorix.user_auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aevorix.user_auth.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByMobileNumber(String mobileNumber);
}

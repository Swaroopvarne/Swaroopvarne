package com.aevorix.user_service.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aevorix.user_service.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByUsername(String username);

	void deleteByUsernameAndItem(String username, String item);
}

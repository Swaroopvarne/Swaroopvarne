package com.aevorix.user_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aevorix.user_service.dto.CartItemDTO;
import com.aevorix.user_service.entity.CartItem;
import com.aevorix.user_service.repo.CartItemRepository;

@Service
public class CartService {

	@Autowired
	private CartItemRepository cartItemRepository;

	public void addToCart(CartItemDTO cartItemDTO, String username) {
		CartItem cartItem = new CartItem();
		cartItem.setItem(cartItemDTO.getItem());
		cartItem.setQuantity(cartItemDTO.getQuantity());
		cartItem.setUsername(username);
		cartItemRepository.save(cartItem);
	}

	public List<CartItem> getCartForUser(String username) {
		return cartItemRepository.findByUsername(username);
	}

	public void removeItem(String username, String itemName) {
		cartItemRepository.deleteByUsernameAndItem(username, itemName);
	}

	public void updateQuantity(String username, String itemName, int newQuantity) {
		List<CartItem> items = cartItemRepository.findByUsername(username);
		for (CartItem item : items) {
			if (item.getItem().equals(itemName)) {
				item.setQuantity(newQuantity);
				cartItemRepository.save(item);
			}
		}
	}
}

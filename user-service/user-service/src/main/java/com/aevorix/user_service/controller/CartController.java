package com.aevorix.user_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aevorix.user_service.dto.CartItemDTO;
import com.aevorix.user_service.entity.CartItem;
import com.aevorix.user_service.reponse.CartResponseDTO;
import com.aevorix.user_service.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

	@Autowired
	private CartService cartService;

	@PostMapping
	public ResponseEntity<CartResponseDTO> addToCart(@RequestBody CartItemDTO cartItem,
			@AuthenticationPrincipal User user) {
		cartService.addToCart(cartItem, user.getUsername());
		return ResponseEntity.ok(new CartResponseDTO("Item added to cart"));
	}

	@GetMapping
	public ResponseEntity<List<CartItem>> getCart(@AuthenticationPrincipal User user) {
		List<CartItem> items = cartService.getCartForUser(user.getUsername());
		return ResponseEntity.ok(items);
	}

	@DeleteMapping("/{itemName}")
	public ResponseEntity<CartResponseDTO> removeItem(@PathVariable String itemName,
			@AuthenticationPrincipal User user) {
		cartService.removeItem(user.getUsername(), itemName);
		return ResponseEntity.ok(new CartResponseDTO("Item removed from cart"));
	}

	@PutMapping("/{itemName}")
	public ResponseEntity<CartResponseDTO> updateQuantity(@PathVariable String itemName, @RequestParam int quantity,
			@AuthenticationPrincipal User user) {
		cartService.updateQuantity(user.getUsername(), itemName, quantity);
		return ResponseEntity.ok(new CartResponseDTO("Item quantity updated"));
	}
}

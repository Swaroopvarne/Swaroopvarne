package com.aevorix.user_service.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private String item;
    private int quantity;
}

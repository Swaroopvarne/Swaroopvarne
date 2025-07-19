package com.aevorix.user_auth.model;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String mobileNo;
    private String password;
    private String role;
}

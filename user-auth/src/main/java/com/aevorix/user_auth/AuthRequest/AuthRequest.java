package com.aevorix.user_auth.AuthRequest;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;

}

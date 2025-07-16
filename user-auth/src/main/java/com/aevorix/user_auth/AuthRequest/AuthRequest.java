package com.aevorix.user_auth.AuthRequest;

import lombok.Data;

@Data
public class AuthRequest {
    private String mobileNo;
    private String password;
    private String email;

}

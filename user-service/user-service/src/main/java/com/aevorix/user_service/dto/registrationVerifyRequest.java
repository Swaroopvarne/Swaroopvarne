package com.aevorix.user_service.dto;

import lombok.Data;

@Data
public class registrationVerifyRequest {
    private String mobileNumber;
    private int otp;
    private String name;
    
}

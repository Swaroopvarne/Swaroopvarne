package com.aevorix.user_auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.aevorix.user_auth.authenticationFilter.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class UserAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthApplication.class, args);
	}

}

package com.aevorix.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.aevorix.common_service.config.JwtProperties;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.aevorix.user_service", "com.aevorix.common_service"})
@EnableConfigurationProperties(JwtProperties.class)
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}

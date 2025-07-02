package com.aevorix.common_service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.aevorix.common_service.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class CommonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommonServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner keepAliveRunner() {
		return args -> {
			System.out.println("Common Service is running. Press Ctrl+C to exit.");
			Thread.currentThread().join(); // Keeps app running
		};
	}
}

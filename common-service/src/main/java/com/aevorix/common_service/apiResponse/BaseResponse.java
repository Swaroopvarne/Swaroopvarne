package com.aevorix.common_service.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
	private int status;
	private String message;
	private T response;
	private LocalDateTime timestamp;

	public static <T> BaseResponse<T> success(String message, T data) {
		return new BaseResponse<>(200, message, data, LocalDateTime.now());
	}

	public static <T> BaseResponse<T> error(int status, String message) {
		return new BaseResponse<>(status, message, null, LocalDateTime.now());
	}
}

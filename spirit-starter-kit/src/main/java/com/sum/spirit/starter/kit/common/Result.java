package com.sum.spirit.starter.kit.common;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
	private static final long serialVersionUID = 1322531227433710974L;
	private Integer code;
	private String msg;
	private T data;

	public static <T> Result<T> success(T data) {
		return new Result<T>(200, "success", data);
	}

	public static <T> Result<T> failed(String message) {
		return new Result<T>(500, message, null);
	}
}

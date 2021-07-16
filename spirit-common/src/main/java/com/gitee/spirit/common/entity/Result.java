package com.gitee.spirit.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

	public Integer code;
	public String message;
	public Object data;

	public Result(Integer code, Object data) {
		this.code = code;
		this.data = data;
	}

	public Result(Object value) {
		this.code = 200;
		this.data = value;
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) data;
	}

}

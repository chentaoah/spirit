package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {

	public CharsState state;
	public Object value;

	public CommonResult(Object value) {
		this.value = value;
	}

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) value;
	}

}

package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {

	public Object value;

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) value;
	}

}

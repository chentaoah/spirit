package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharsResult {

	public Object payload;

	@SuppressWarnings("unchecked")
	public <T> T get() {
		return (T) payload;
	}

}

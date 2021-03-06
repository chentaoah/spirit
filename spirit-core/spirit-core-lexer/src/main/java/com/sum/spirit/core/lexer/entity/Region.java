package com.sum.spirit.core.lexer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {

	public int startIndex;
	public int endIndex;

	public int size() {
		return endIndex - startIndex;
	}

}

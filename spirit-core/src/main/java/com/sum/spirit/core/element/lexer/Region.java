package com.sum.spirit.core.element.lexer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region {

	public int startIndex;
	public int endIndex;

	public int size() {
		return endIndex - startIndex;
	}

}

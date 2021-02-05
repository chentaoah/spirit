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

	public Region getStartBorder() {
		return new Region(startIndex, startIndex + 1);
	}

	public Region getEndBorder() {
		return new Region(endIndex - 1, endIndex);
	}

	public int size() {
		return endIndex - startIndex;
	}

	public Region shift(int shift) {
		startIndex += shift;
		endIndex += shift;
		return this;
	}
}

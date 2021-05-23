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

	public boolean contains(int index) {
		return startIndex <= index && index < endIndex;
	}

	public boolean isAfter(Region region) {
		return startIndex >= region.endIndex;
	}

	public boolean isOverlap(Region region) {
		return contains(region.startIndex) || contains(region.endIndex - 1);
	}

}

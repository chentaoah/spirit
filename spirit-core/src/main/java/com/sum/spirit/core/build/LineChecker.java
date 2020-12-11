package com.sum.spirit.core.build;

import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.element.impl.Line;

@Component
public class LineChecker {

	public void check(Line line) {
		if (line.text.endsWith(";")) {
			throw new RuntimeException("Please do not end with a colon!");
		}
	}

}

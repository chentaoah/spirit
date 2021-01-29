package com.sum.spirit.core.element.handler;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.element.pojo.Line;

@Component
public class LineChecker {

	public void check(Line line) {
		if (line.text.endsWith(";")) {
			throw new RuntimeException("Please do not end with a colon!");
		}
	}

}

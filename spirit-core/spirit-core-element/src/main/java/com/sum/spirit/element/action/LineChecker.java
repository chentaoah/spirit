package com.sum.spirit.element.action;

import org.springframework.stereotype.Component;

import com.sum.spirit.element.entity.Line;

@Component
public class LineChecker {

	public void check(Line line) {
		if (line.text.endsWith(";")) {
			throw new RuntimeException("Please do not end with a colon!");
		}
	}

}

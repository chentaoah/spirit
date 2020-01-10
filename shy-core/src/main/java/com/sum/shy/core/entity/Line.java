package com.sum.shy.core.entity;

import com.sum.shy.utils.LineUtils;

public class Line {

	public Integer number;

	public String text;

	public Line(Integer number, String text) {
		this.number = number;
		this.text = text;
	}

	public boolean isIgnore() {
		return text.trim().startsWith("//") || text.trim().length() == 0;
	}

	@Override
	public String toString() {
		return number + ":" + LineUtils.getSpaceByNumber(6 - (number + ":").length()) + text;
	}

}

package com.sum.shy.core.doc;

import com.sum.shy.core.utils.LineUtils;

public class Line {

	public int number;

	public String text;

	public Line(int number, String text) {
		this.number = number;
		this.text = text;
	}

	public Line(String text) {
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

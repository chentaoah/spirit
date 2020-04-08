package com.sum.shy.core.document;

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

	public boolean hasChild() {
		return text.trim().endsWith("{");
	}

	public String getIndent() {
		char firstChar = text.trim().charAt(0);
		return text.substring(0, text.indexOf(firstChar));
	}

	public String debug() {
		return number + ":" + LineUtils.getSpace(6 - (number + ":").length()) + text;
	}

	@Override
	public String toString() {
		return text;
	}

}

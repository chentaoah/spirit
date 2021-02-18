package com.sum.spirit.element.entity;

import com.sum.spirit.common.utils.LineUtils;

public class Line {

	public int number;

	public String text;

	public Line(String text) {
		this.text = text;
	}

	public Line(int number, String text) {
		this.number = number;
		this.text = text;
	}

	public boolean isIgnore() {
		return text.trim().startsWith("//") || text.trim().length() == 0;
	}

	public boolean hasChild() {
		return text.trim().endsWith("{");
	}

	public boolean isEnding() {
		return text.trim().startsWith("}");
	}

	public String getIndent() {
		char firstChar = text.trim().charAt(0);
		return text.substring(0, text.indexOf(firstChar));
	}

	public void debug() {
		System.out.println(number + ":" + LineUtils.getSpaces(6 - (number + ":").length()) + text);
	}

	@Override
	public String toString() {
		return text;
	}

}

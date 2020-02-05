package com.sum.shy.core.document;

import java.util.ArrayList;

import com.sum.shy.core.entity.Line;

@SuppressWarnings("serial")
public class Element extends ArrayList<Element> {

	public Line line;

	public Element(Line line) {
		this.line = line;
	}

	public boolean hasChild() {
		return line.text.trim().endsWith("{");
	}

	public void show() {
		System.out.println(line);
	}

}

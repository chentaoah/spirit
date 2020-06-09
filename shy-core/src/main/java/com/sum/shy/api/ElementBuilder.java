package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

@Service("element_builder")
public interface ElementBuilder {

	Element buildElement(Line line);

	default Element buildElement(String text) {
		return buildElement(new Line(text));
	}

}

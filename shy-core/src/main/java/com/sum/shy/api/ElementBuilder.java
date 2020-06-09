package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

@Service("element_builder")
public interface ElementBuilder {

	Element build(Line line);

	default Element build(String text) {
		return build(new Line(text));
	}

}

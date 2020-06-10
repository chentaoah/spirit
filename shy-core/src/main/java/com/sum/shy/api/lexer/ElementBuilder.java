package com.sum.shy.api.lexer;

import com.sum.pisces.api.Service;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

@Service("element_builder")
public interface ElementBuilder {

	default Element build(String text) {
		return build(new Line(text));
	}

	Element build(Line line);

}

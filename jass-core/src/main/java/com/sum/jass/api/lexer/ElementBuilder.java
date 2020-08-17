package com.sum.jass.api.lexer;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.element.Element;
import com.sum.jass.pojo.element.Line;

@Service("element_builder")
public interface ElementBuilder {

	default Element build(String text) {
		return build(new Line(text));
	}

	Element build(Line line);

}

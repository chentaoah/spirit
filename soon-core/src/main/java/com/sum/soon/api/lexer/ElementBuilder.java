package com.sum.soon.api.lexer;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.element.Element;
import com.sum.soon.pojo.element.Line;

@Service("element_builder")
public interface ElementBuilder {

	default Element build(String text) {
		return build(new Line(text));
	}

	Element build(Line line);

}

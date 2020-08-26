package com.sum.spirit.api.lexer;

import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;

public interface ElementBuilder {

	default Element build(String text) {
		return build(new Line(text));
	}

	Element build(Line line);

}

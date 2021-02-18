package com.sum.spirit.core.element.api;

import com.sum.spirit.core.element.entity.Element;
import com.sum.spirit.core.element.entity.Line;
import com.sum.spirit.core.element.entity.Statement;

public interface ElementBuilder {

	Element build(String text);

	Element build(Line line);

	Element rebuild(Statement statement);

}

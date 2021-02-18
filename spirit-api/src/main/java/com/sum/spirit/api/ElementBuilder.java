package com.sum.spirit.api;

import com.sum.spirit.common.entity.Element;
import com.sum.spirit.common.entity.Line;
import com.sum.spirit.common.entity.Statement;

public interface ElementBuilder {

	Element build(String text);

	Element build(Line line);

	Element rebuild(Statement statement);

}

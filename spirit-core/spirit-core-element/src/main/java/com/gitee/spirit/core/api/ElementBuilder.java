package com.gitee.spirit.core.api;

import com.gitee.spirit.core.element.entity.Element;
import com.gitee.spirit.core.element.entity.Line;
import com.gitee.spirit.core.element.entity.Statement;

public interface ElementBuilder {

	Element build(String text);

	Element build(Line line);

	Element build(Statement statement);

}

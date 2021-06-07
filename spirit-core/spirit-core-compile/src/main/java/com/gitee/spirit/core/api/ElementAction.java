package com.gitee.spirit.core.api;

import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;

public interface ElementAction {

	void visitElement(VisitContext context, Element element);

}

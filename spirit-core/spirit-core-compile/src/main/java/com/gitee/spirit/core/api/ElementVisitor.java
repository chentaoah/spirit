package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.VisitContext;
import com.gitee.spirit.core.element.entity.Element;

public interface ElementVisitor {

	IVariable visitElement(VisitContext context, Element element);

}

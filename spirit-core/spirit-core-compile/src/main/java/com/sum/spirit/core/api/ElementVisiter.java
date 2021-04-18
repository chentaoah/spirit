package com.sum.spirit.core.api;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.compile.entity.MethodContext;
import com.sum.spirit.core.element.entity.Element;

public interface ElementVisiter {

	IVariable visitElement(IClass clazz, Element element);

	IVariable visitElement(IClass clazz, Element element, MethodContext context);

}

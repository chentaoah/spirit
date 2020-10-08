package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;

public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

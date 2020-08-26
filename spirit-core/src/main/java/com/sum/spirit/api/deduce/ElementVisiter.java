package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;

public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

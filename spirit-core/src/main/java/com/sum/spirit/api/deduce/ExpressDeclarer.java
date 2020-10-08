package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.MethodContext;

public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

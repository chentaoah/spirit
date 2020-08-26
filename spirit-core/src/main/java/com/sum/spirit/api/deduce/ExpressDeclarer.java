package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;

public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

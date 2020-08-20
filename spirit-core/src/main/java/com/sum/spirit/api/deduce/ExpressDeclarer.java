package com.sum.spirit.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Element;

@Service("express_declarer")
public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

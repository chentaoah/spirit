package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.common.MethodContext;
import com.sum.jass.pojo.element.Element;

@Service("express_declarer")
public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

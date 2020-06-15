package com.sum.shy.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.common.MethodContext;
import com.sum.shy.pojo.element.Element;

@Service("express_declarer")
public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

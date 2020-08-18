package com.sum.slimx.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;
import com.sum.slimx.pojo.common.MethodContext;
import com.sum.slimx.pojo.element.Element;

@Service("express_declarer")
public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

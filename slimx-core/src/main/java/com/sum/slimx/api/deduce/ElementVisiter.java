package com.sum.slimx.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;
import com.sum.slimx.pojo.clazz.IVariable;
import com.sum.slimx.pojo.common.MethodContext;
import com.sum.slimx.pojo.element.Element;

@Service("element_visiter")
public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

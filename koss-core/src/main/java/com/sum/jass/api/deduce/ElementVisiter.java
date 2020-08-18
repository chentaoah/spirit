package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.clazz.IVariable;
import com.sum.jass.pojo.common.MethodContext;
import com.sum.jass.pojo.element.Element;

@Service("element_visiter")
public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

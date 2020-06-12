package com.sum.shy.api.deduce;

import com.sum.pisces.api.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IVariable;
import com.sum.shy.pojo.common.MethodContext;
import com.sum.shy.pojo.element.Element;

@Service("element_visiter")
public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

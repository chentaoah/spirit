package com.sum.shy.api.deducer;

import com.sum.pisces.api.Service;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IVariable;
import com.sum.shy.element.Element;

@Service("element_visiter")
public interface ElementVisiter {

	IVariable visit(IClass clazz, MethodContext context, Element element);

}

package com.sum.shy.api.deducer;

import com.sum.pisces.api.Service;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Element;

@Service("express_declarer")
public interface ExpressDeclarer {

	void declare(IClass clazz, MethodContext context, Element element);

}

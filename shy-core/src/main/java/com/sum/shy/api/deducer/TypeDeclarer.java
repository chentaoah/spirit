package com.sum.shy.api.deducer;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

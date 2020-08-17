package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

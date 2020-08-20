package com.sum.spirit.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

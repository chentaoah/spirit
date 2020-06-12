package com.sum.shy.api.deduce;

import com.sum.pisces.api.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

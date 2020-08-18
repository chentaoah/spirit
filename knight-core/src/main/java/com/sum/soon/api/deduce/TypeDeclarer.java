package com.sum.soon.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

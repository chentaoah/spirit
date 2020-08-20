package com.sum.slimx.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;
import com.sum.slimx.pojo.element.Element;

@Service("type_declarer")
public interface TypeDeclarer {

	void declare(IClass clazz, Element element);

}

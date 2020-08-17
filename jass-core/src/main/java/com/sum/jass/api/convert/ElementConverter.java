package com.sum.jass.api.convert;

import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Element;

public interface ElementConverter {

	void convert(IClass clazz, Element element);

}

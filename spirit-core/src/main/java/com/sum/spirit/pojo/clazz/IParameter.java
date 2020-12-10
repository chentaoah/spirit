package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.pojo.clazz.api.Annotated;
import com.sum.spirit.pojo.element.Element;

public class IParameter extends Annotated {

	public IParameter(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public String getName() {
		if (element.isDeclare()) {
			return element.getStr(1);
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

}

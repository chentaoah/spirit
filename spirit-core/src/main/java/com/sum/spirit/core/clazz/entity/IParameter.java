package com.sum.spirit.core.clazz.entity;

import java.util.List;

import com.sum.spirit.core.clazz.frame.AnnotationUnit;
import com.sum.spirit.element.entity.Element;

public class IParameter extends AnnotationUnit {

	public IParameter(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public String getName() {
		if (element.isDeclare()) {
			return element.getStr(1);
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

	@Override
	public String toString() {
		return element.toString();
	}

}

package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.pojo.element.Element;

public class IField extends IMember {

	public IField(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	@Override
	public String getName() {
		if (element.isDeclare() || element.isDeclareAssign()) {
			return element.getStr(1);

		} else if (element.isAssign()) {
			return element.getStr(0);
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

}

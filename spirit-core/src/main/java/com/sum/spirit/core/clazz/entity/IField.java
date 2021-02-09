package com.sum.spirit.core.clazz.entity;

import java.util.List;

import com.sum.spirit.core.clazz.frame.MemberUnit;
import com.sum.spirit.element.entity.Element;

public class IField extends MemberUnit {

	public IField(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public String getName() {
		if (element.isDeclare() || element.isDeclareAssign()) {
			return element.getStr(1);

		} else if (element.isAssign()) {
			return element.getStr(0);
		}
		throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
	}

}

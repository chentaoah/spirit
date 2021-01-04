package com.sum.spirit.pojo.clazz.impl;

import java.util.List;

import com.sum.spirit.pojo.clazz.api.MemberUnit;
import com.sum.spirit.pojo.element.impl.Element;

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

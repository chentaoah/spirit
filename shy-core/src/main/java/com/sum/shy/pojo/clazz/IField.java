package com.sum.shy.pojo.clazz;

import java.util.List;

import com.sum.shy.pojo.element.Element;

public class IField extends AbsMember {

	public IField(List<IAnnotation> annotations, boolean isStatic, Element element) {

		super(annotations, isStatic, element);

		if (element.isDeclare() || element.isDeclareAssign()) {
			name = element.getStr(1);

		} else if (element.isAssign()) {
			name = element.getStr(0);

		} else {
			throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
		}

	}

}

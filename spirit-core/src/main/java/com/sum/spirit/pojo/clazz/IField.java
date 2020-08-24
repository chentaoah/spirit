package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.pojo.element.Element;

public class IField extends AbsMember {

	public IField(List<IAnnotation> annotations, Element element) {

		super(annotations, element);

		if (element.isDeclare() || element.isDeclareAssign()) {
			name = element.getStr(1);

		} else if (element.isAssign()) {
			name = element.getStr(0);

		} else {
			throw new RuntimeException("Unsupported syntax!syntax:" + element.syntax);
		}
	}
}

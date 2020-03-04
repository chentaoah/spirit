package com.sum.shy.core.clazz;

import java.util.List;

import com.sum.shy.core.doc.Element;

public class IField extends AbsMember {

	public IField(List<Element> annotations, boolean isStatic, Element element) {
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

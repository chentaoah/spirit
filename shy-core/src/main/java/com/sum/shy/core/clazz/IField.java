package com.sum.shy.core.clazz;

import java.util.List;

import com.sum.shy.core.doc.Element;

public class IField extends AbsMember {

	public IField(List<Element> annotations, boolean isStatic, Element element) {
		super(annotations, isStatic, element);
	}

	public String getName() {
		return null;
	}

}

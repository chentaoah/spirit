package com.sum.spirit.core.clazz.frame;

import java.util.List;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.element.entity.Element;

public abstract class MemberUnit extends AnnotationUnit {

	public volatile boolean isLock = false;

	public MemberUnit(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public boolean isStatic() {
		return element.isModified(KeywordEnum.STATIC.value);
	}

	public void lock() {
		if (isLock) {
			throw new RuntimeException("There is a circular dependency!");
		}
		isLock = true;
	}

	public void unLock() {
		isLock = false;
	}

}

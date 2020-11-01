package com.sum.spirit.pojo.clazz;

import java.util.List;

import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.enums.KeywordEnum;

public abstract class Member extends Annotated {

	public volatile boolean isLock = false;

	public Member(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

	public boolean isStatic() {
		return element.isModified(KeywordEnum.STATIC.value);
	}

	public void lock() {
		if (isLock)
			throw new RuntimeException("There is a circular dependency!name:" + getName());
		isLock = true;
	}

	public void unLock() {
		isLock = false;
	}

}

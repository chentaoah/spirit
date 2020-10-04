package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.common.KeywordEnum;
import com.sum.spirit.pojo.element.Element;

public abstract class AbsMember {

	public List<IAnnotation> annotations;
	public Element element;
	public IType type;
	public String name;
	public volatile boolean isLock = false;

	public AbsMember(List<IAnnotation> annotations, Element element) {
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
		this.element = element;
	}

	public boolean isStatic() {
		return element.isModified(KeywordEnum.STATIC.value);
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		this.type = type;
	}

	public void lock() {
		if (isLock)
			throw new RuntimeException("There is a circular dependency!name:" + name);
		isLock = true;
	}

	public void unLock() {
		isLock = false;
	}

}

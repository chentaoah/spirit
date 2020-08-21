package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Element;

public abstract class AbsMember {
	// 注解
	public List<IAnnotation> annotations;
	// 节点
	public Element element;
	// 类型
	public IType type;
	// 名称
	public String name;
	// 锁
	public volatile boolean isLock = false;

	public AbsMember(List<IAnnotation> annotations, Element element) {
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
		this.element = element;
	}

	public boolean isStatic() {
		return element.isModified(Constants.STATIC_KEYWORD);
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

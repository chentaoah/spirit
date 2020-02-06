package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.doc.Element;
import com.sum.shy.core.type.api.IType;

public abstract class AbsMember {
	// 注解
	public List<Element> annotations;
	// 是否静态
	public boolean isStatic;
	// 节点
	public Element element;
	// 类型
	public IType type;
	// 锁
	public volatile boolean isLock = false;

	public AbsMember(List<Element> annotations, boolean isStatic, Element element) {
		this.annotations = new ArrayList<>(annotations);// 拷贝一份
		this.isStatic = isStatic;
		this.element = element;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		this.type = type;
	}

	public void lock() {
		if (isLock)
			throw new RuntimeException("There is a circular dependency!" + toString());
		isLock = true;
	}

	public void unLock() {
		isLock = false;
	}

}

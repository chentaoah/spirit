package com.sum.shy.core.entity;

import com.google.common.base.Joiner;
import com.sum.shy.core.api.Type;

public abstract class AbsType implements Type {

	@Override
	public boolean isGenericType() {
		return getGenericTypes().size() > 0;
	}

	@Override
	public boolean isArray() {
		return getTypeName().endsWith("[]");
	}

	@Override
	public boolean isStr() {
		return "String".equals(getTypeName());
	}

	@Override
	public boolean isList() {
		return "List".equals(getTypeName());
	}

	@Override
	public boolean isMap() {
		return "Map".equals(getTypeName());
	}

	@Override
	public String toString() {
		if (!isArray() && !isGenericType()) {// 普通类型
			return getTypeName();

		} else if (isArray() && !isGenericType()) {// 数组
			return getTypeName();

		} else if (!isArray() && isGenericType()) {// 泛型
			return getTypeName() + "<" + Joiner.on(",").join(getGenericTypes()) + ">";

		}
		return null;
	}
}

package com.sum.shy.core.entity;

import com.google.common.base.Joiner;
import com.sum.shy.core.api.Type;

public abstract class AbsType implements Type {

	@Override
	public String getName() {
		return getSimpleName().replace("[]", "");
	}

	@Override
	public boolean isGenericType() {
		return getGenericTypes().size() > 0;
	}

	@Override
	public boolean isArray() {
		return getSimpleName().endsWith("[]");
	}

	@Override
	public boolean isStr() {
		return "String".equals(getSimpleName());
	}

	@Override
	public boolean isList() {
		return "List".equals(getSimpleName());
	}

	@Override
	public boolean isMap() {
		return "Map".equals(getSimpleName());
	}

	@Override
	public String toString() {
		if (!isArray() && !isGenericType()) {// 普通类型
			return getSimpleName();

		} else if (isArray() && !isGenericType()) {// 数组
			return getSimpleName();

		} else if (!isArray() && isGenericType()) {// 泛型
			return getSimpleName() + "<" + Joiner.on(", ").join(getGenericTypes()) + ">";

		}
		return null;
	}
}

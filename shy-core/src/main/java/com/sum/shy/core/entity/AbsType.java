package com.sum.shy.core.entity;

import com.google.common.base.Joiner;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.utils.ReflectUtils;

public abstract class AbsType implements Type {

	@Override
	public String getTypeName() {
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
		// 当一个类型被拼接到java代码中的时候，在该类中尝试自动引入
		String finalName = Context.get().currentClass.addImport(getClassName()) ? getSimpleName()
				: ReflectUtils.getClassName(getClassName());
		if (!isArray() && !isGenericType()) {// 普通类型
			return finalName;
		} else if (isArray() && !isGenericType()) {// 数组
			return finalName;
		} else if (!isArray() && isGenericType()) {// 泛型
			return finalName + "<" + Joiner.on(", ").join(getGenericTypes()) + ">";
		}
		return null;
	}
}

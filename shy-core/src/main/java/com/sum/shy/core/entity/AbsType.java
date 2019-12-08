package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.utils.ReflectUtils;

public abstract class AbsType implements Type {

	public List<Type> genericTypes = new ArrayList<>();

	@Override
	public String getTypeName() {
		return getSimpleName().replace("[]", "");
	}

	@Override
	public List<Type> getGenericTypes() {
		return genericTypes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Type) {
			Type type = (Type) obj;
			boolean flag = getClassName().equals(type.getClassName());
			if (flag) {
				int count = 0;
				for (Type genericType : getGenericTypes()) {
					if (!genericType.equals(type.getGenericTypes().get(count++))) {
						flag = false;
						break;
					}
				}
			}
			return flag;
		}
		return false;
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

		String finalName = getSimpleName();
		CtClass currentClass = Context.get().currentClass;// 当前类
		if (currentClass != null) {
			if (!currentClass.addImport(getClassName()))
				finalName = ReflectUtils.getClassName(getClassName()) + (isArray() ? "[]" : "");
		}

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

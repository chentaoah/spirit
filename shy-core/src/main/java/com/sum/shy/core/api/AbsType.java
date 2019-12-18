package com.sum.shy.core.api;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.utils.ReflectUtils;

public abstract class AbsType implements Type {

	public CtClass clazz;

	public List<Type> genericTypes = new ArrayList<>();

	/**
	 * 要求子类必须传入CtClass
	 * 
	 * @param clazz
	 */
	public AbsType(CtClass clazz) {
		this.clazz = clazz;
	}

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
	public boolean isWildcard() {
		return getClassName().equals(WildcardType.class.getName());
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
		// 最终是否打印类全名，看是否能够添加到该类型中
		String finalName = null;
		if (isWildcard()) {
			finalName = "?";
		} else {
			if (clazz.addImport(getClassName())) {
				finalName = getSimpleName();
			} else {
				finalName = ReflectUtils.getClassName(getClassName()) + (isArray() ? "[]" : "");
			}
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

package com.sum.shy.type.api;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;
import com.sum.shy.clazz.IClass;
import com.sum.shy.utils.TypeUtils;

public abstract class AbsType implements Type {

	public IClass clazz;

	public List<Type> genericTypes = new ArrayList<>();

	/**
	 * 要求子类必须传入IClass
	 * 
	 * @param clazz
	 */
	public AbsType(IClass clazz) {
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
	public boolean isPrimitive() {
		switch (getClassName()) {
		case "boolean":
			return true;
		case "char":
			return true;
		case "short":
			return true;
		case "int":
			return true;
		case "long":
			return true;
		case "float":
			return true;
		case "double":
			return true;
		case "byte":
			return true;
		}
		return false;
	}

	@Override
	public boolean isArray() {
		return getSimpleName().endsWith("[]");
	}

	@Override
	public boolean isGenericType() {
		return getGenericTypes().size() > 0;
	}

	@Override
	public boolean isWildcard() {
		return WildcardType.class.getName().equals(getClassName());
	}

	@Override
	public boolean isVoid() {
		return void.class.getName().equals(getClassName());
	}

	@Override
	public boolean isObj() {
		return Object.class.getName().equals(getClassName());
	}

	@Override
	public boolean isStr() {
		return String.class.getName().equals(getClassName());
	}

	@Override
	public boolean isList() {
		return List.class.getName().equals(getClassName());
	}

	@Override
	public boolean isMap() {
		return Map.class.getName().equals(getClassName());
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
				finalName = TypeUtils.removeDecoration(getClassName()) + (isArray() ? "[]" : "");
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

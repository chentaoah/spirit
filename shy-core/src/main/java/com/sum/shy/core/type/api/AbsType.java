package com.sum.shy.core.type.api;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.common.base.Joiner;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.lexical.SemanticDelegate;
import com.sum.shy.core.utils.TypeUtils;

public abstract class AbsType implements IType {

	public static final Pattern BASIC_TYPE_PATTERN = Pattern.compile("^(" + SemanticDelegate.BASIC_TYPE_ENUM + ")$");

	public IClass clazz;

	public List<IType> genericTypes = new ArrayList<>();

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
	public List<IType> getGenericTypes() {
		return genericTypes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IType) {
			IType type = (IType) obj;
			boolean flag = getClassName().equals(type.getClassName());
			if (flag) {
				int count = 0;
				for (IType genericType : getGenericTypes()) {
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
		return BASIC_TYPE_PATTERN.matcher(getClassName()).matches();
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
	public String getSuperName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isAssignableFrom(IType type) {
		// TODO Auto-generated method stub
		return false;
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

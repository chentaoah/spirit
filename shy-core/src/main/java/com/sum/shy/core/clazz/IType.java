package com.sum.shy.core.clazz;

import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IType {

	public String className;
	public String simpleName;
	public String typeName;
	public IType superClass;
	public List<IType> interfaces;
	public boolean isPrimitive;
	public boolean isArray;
	public boolean isGenericType;
	public List<IType> genericTypes = new ArrayList<>();
	public boolean isWildcard;
	public IClass from;
	public boolean isNative;

	public boolean isWildcard() {
		return WildcardType.class.getName().equals(className);
	}

	public boolean isVoid() {
		return void.class.getName().equals(className);
	}

	public boolean isObj() {
		return Object.class.getName().equals(className);
	}

	public boolean isStr() {
		return String.class.getName().equals(className);
	}

	public boolean isList() {
		return List.class.getName().equals(className);
	}

	public boolean isMap() {
		return Map.class.getName().equals(className);
	}

	public boolean isAssignableFrom(IType type) {
		// TODO Auto-generated method stub
		return false;
	}

}

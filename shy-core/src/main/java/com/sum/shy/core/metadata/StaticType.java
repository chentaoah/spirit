package com.sum.shy.core.metadata;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.type.TypeFactory;

public class StaticType {

	public static final IType WILDCARD_TYPE = new IType();
	public static final IType BOOLEAN_TYPE = TypeFactory.createNativeType(boolean.class);
	public static final IType INT_TYPE = TypeFactory.createNativeType(int.class);
	public static final IType LONG_TYPE = TypeFactory.createNativeType(long.class);
	public static final IType DOUBLE_TYPE = TypeFactory.createNativeType(double.class);
	public static final IType OBJECT_TYPE = TypeFactory.createNativeType(Object.class);
	public static final IType STRING_TYPE = TypeFactory.createNativeType(String.class);

	static {
		// 未知类型
		WILDCARD_TYPE.setClassName(null);
		WILDCARD_TYPE.setSimpleName(null);
		WILDCARD_TYPE.setTypeName(null);
		WILDCARD_TYPE.setPrimitive(false);
		WILDCARD_TYPE.setArray(false);
		WILDCARD_TYPE.setGenericTypes(null);
		WILDCARD_TYPE.setWildcard(true);
		WILDCARD_TYPE.setDeclarer(null);
		WILDCARD_TYPE.setNative(false);

	}

}

package com.sum.shy.core.metadata;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.type.TypeFactory;
import com.sum.shy.lib.Collection;

public class StaticType {

	public static final IType VOID_TYPE = TypeFactory.createNativeType(void.class);
	public static final IType WILDCARD_TYPE = TypeFactory.createNativeType(Object.class);
	public static final IType BOOLEAN_TYPE = TypeFactory.createNativeType(boolean.class);
	public static final IType INT_TYPE = TypeFactory.createNativeType(int.class);
	public static final IType LONG_TYPE = TypeFactory.createNativeType(long.class);
	public static final IType DOUBLE_TYPE = TypeFactory.createNativeType(double.class);
	public static final IType OBJECT_TYPE = TypeFactory.createNativeType(Object.class);
	public static final IType STRING_TYPE = TypeFactory.createNativeType(String.class);
	public static final IType CLASS_TYPE = TypeFactory.createNativeType(Class.class);

	static {
		WILDCARD_TYPE.setWildcard(true);// ?
		CLASS_TYPE.setGenericTypes(Collection.newArrayList(WILDCARD_TYPE));// Class<?>
	}

}

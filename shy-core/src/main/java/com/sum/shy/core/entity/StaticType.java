package com.sum.shy.core.entity;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.clazz.type.TypeFactory;
import com.sum.shy.lib.Collection;

public class StaticType {

	public static final IType VOID_TYPE = TypeFactory.create(void.class);
	public static final IType BOOLEAN_TYPE = TypeFactory.create(boolean.class);
	public static final IType CHAR_TYPE = TypeFactory.create(char.class);
	public static final IType INT_TYPE = TypeFactory.create(int.class);
	public static final IType LONG_TYPE = TypeFactory.create(long.class);
	public static final IType DOUBLE_TYPE = TypeFactory.create(double.class);
	public static final IType NULL_TYPE = TypeFactory.create(Object.class);
	public static final IType WILDCARD_TYPE = TypeFactory.create(Object.class);
	public static final IType OBJECT_TYPE = TypeFactory.create(Object.class);
	public static final IType STRING_TYPE = TypeFactory.create(String.class);
	public static final IType CLASS_TYPE = TypeFactory.create(Class.class);

	static {
		NULL_TYPE.setNull(true);// null
		WILDCARD_TYPE.setWildcard(true);// ?
		CLASS_TYPE.setGenericTypes(Collection.newArrayList(WILDCARD_TYPE));// Class<?>
	}

}

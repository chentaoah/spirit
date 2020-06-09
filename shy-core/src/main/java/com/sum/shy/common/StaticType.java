package com.sum.shy.common;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.TypeFactory;
import com.sum.shy.clazz.IType;
import com.sum.shy.lib.Collection;

public class StaticType {

	public static final TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public static final IType VOID_TYPE = factory.create(void.class);
	public static final IType BOOLEAN_TYPE = factory.create(boolean.class);
	public static final IType CHAR_TYPE = factory.create(char.class);
	public static final IType INT_TYPE = factory.create(int.class);
	public static final IType LONG_TYPE = factory.create(long.class);
	public static final IType DOUBLE_TYPE = factory.create(double.class);
	public static final IType NULL_TYPE = factory.create(Object.class);
	public static final IType WILDCARD_TYPE = factory.create(Object.class);
	public static final IType OBJECT_TYPE = factory.create(Object.class);
	public static final IType STRING_TYPE = factory.create(String.class);
	public static final IType CLASS_TYPE = factory.create(Class.class);

	static {
		NULL_TYPE.setNull(true);// null
		WILDCARD_TYPE.setWildcard(true);// ?
		CLASS_TYPE.setGenericTypes(Collection.newArrayList(WILDCARD_TYPE));// Class<?>
	}

}

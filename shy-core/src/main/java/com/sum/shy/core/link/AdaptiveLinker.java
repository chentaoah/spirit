package com.sum.shy.core.link;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.link.ClassLinker;
import com.sum.shy.api.link.TypeFactory;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.common.TypeTable;

public class AdaptiveLinker implements ClassLinker {

	public static final String ARRAY_LENGTH = "length";

	public static ClassLinker codeLinker = ProxyFactory.cast(ClassLinker.class, "code_linker");
	public static ClassLinker nativeLinker = ProxyFactory.cast(ClassLinker.class, "native_linker");
	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public <T> T toClass(IType type) {
		return !type.isNative() ? codeLinker.toClass(type) : nativeLinker.toClass(type);
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return !type.isNative() ? codeLinker.getTypeVariableIndex(type, genericName) : nativeLinker.getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		return !type.isNative() ? codeLinker.getSuperType(type) : nativeLinker.getSuperType(type);
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		return !type.isNative() ? codeLinker.getInterfaceTypes(type) : nativeLinker.getInterfaceTypes(type);
	}

	@Override
	public IType visitField(IType type, String fieldName) {

		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(fieldName, "Field name cannot be empty!");

		// xxx.class class是关键字
		if (Constants.CLASS_KEYWORD.equals(fieldName))
			return factory.create(Class.class, type.getWrappedType());

		// 原始类型没有属性和方法
		if (type.isPrimitive())
			throw new RuntimeException("The primitive type has no field!");

		// 访问数组length直接返回int类型
		if (type.isArray() && ARRAY_LENGTH.equals(fieldName))
			return TypeTable.INT_TYPE;

		// 这里设定Object没有属性
		if (type.isObj())
			return null;

		IType returnType = !type.isNative() ? codeLinker.visitField(type, fieldName) : nativeLinker.visitField(type, fieldName);

		if (returnType == null) {
			IType superType = type.getSuperType();
			if (superType != null)
				return visitField(superType, fieldName);
		}

		Assert.notNull(returnType, "Return type cannot be null!");
		return returnType;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(methodName, "Method name cannot be empty!");

		// super()和this()指代父类或者本身的构造函数，返回这个类本身
		if (Constants.SUPER_KEYWORD.equals(methodName) || Constants.THIS_KEYWORD.equals(methodName))
			return type;

		// 原始类型没有属性和方法
		if (type.isPrimitive())
			throw new RuntimeException("The primitive type has no method!");

		// 原始类型没有属性和方法
		if (type.isArray())
			throw new RuntimeException("Array has no method!");

		IType returnType = !type.isNative() ? codeLinker.visitMethod(type, methodName, parameterTypes)
				: nativeLinker.visitMethod(type, methodName, parameterTypes);

		if (returnType == null) {
			IType superType = type.getSuperType();
			if (superType != null)
				return visitMethod(superType, methodName, parameterTypes);
		}

		Assert.notNull(returnType, "Return type cannot be null!");
		return returnType;
	}

}

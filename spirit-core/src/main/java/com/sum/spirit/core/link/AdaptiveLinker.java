package com.sum.spirit.core.link;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.pojo.exception.NoSuchFieldException;
import com.sum.spirit.pojo.exception.NoSuchMethodException;

@Component
@Primary
public class AdaptiveLinker implements ClassLinker {

	public static final String ARRAY_LENGTH = "length";

	@Autowired
	@Qualifier("codeLinker")
	public ClassLinker codeLinker;
	@Autowired
	@Qualifier("nativeLinker")
	public ClassLinker nativeLinker;
	@Autowired
	public TypeFactory factory;

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
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {

		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(fieldName, "Field name cannot be empty!");

		// xxx.class class是关键字
		if (KeywordEnum.CLASS.value.equals(fieldName))
			return factory.create(TypeEnum.CLASS.value.getClassName(), type.getWrappedType());

		// 原始类型没有属性和方法
		if (type.isPrimitive())
			throw new RuntimeException("The primitive type has no field!");

		// 访问数组length直接返回int类型
		if (type.isArray() && AdaptiveLinker.ARRAY_LENGTH.equals(fieldName))
			return TypeEnum.INT.value;

		IType returnType = !type.isNative() ? codeLinker.visitField(type, fieldName) : nativeLinker.visitField(type, fieldName);

		if (returnType == null) {
			IType superType = type.getSuperType();
			if (superType != null)
				return visitField(superType, fieldName);
		}

		if (returnType == null)
			throw new NoSuchFieldException(String.format("No such field!className:%s, fieldName:%s", type.getClassName(), fieldName));

		return returnType;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {

		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(methodName, "Method name cannot be empty!");

		// super()和this()指代父类或者本身的构造函数，返回这个类本身
		if (KeywordEnum.SUPER.value.equals(methodName) || KeywordEnum.THIS.value.equals(methodName))
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

		if (returnType == null)
			throw new NoSuchMethodException(String.format("No such method!className:%s, methodName:%s", type.getClassName(), methodName));

		return returnType;
	}

}

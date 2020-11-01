package com.sum.spirit.core.type;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.ModifierEnum;
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

		if (type.isPrimitive())
			return null;

		if (type.isArray())
			return TypeEnum.OBJECT.value;

		IType superType = !type.isNative() ? codeLinker.getSuperType(type) : nativeLinker.getSuperType(type);
		if (superType == null)
			return null;

		int modifiers = type.getModifiers();
		if (modifiers == ModifierEnum.THIS.value || modifiers == ModifierEnum.SUPER.value) {
			superType.setModifiers(ModifierEnum.SUPER.value);

		} else if (modifiers == ModifierEnum.PUBLIC.value) {
			superType.setModifiers(ModifierEnum.PUBLIC.value);
		}

		return superType;
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {

		if (type.isPrimitive())
			return new ArrayList<>();

		if (type.isArray())
			return new ArrayList<>();

		return !type.isNative() ? codeLinker.getInterfaceTypes(type) : nativeLinker.getInterfaceTypes(type);
	}

	@Override
	public boolean isMoreAbstract(IType abstractType, IType type) {

		if (type == null)
			return false;

		// Null can not match any type
		if (abstractType.isNull())
			return false;

		// Any type can match null
		if (type.isNull())
			return true;

		// 这个方法还要判断泛型
		if (type.equals(abstractType))
			return true;

		// 这个方法中，还要考虑到自动拆组包
		if (isMoreAbstract(abstractType, getSuperType(type.getWrappedType())))
			return true;

		// 接口
		for (IType inter : getInterfaceTypes(type)) {
			if (isMoreAbstract(abstractType, inter))
				return true;
		}

		return false;
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
			IType superType = getSuperType(type);
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
			IType superType = getSuperType(type);
			if (superType != null)
				return visitMethod(superType, methodName, parameterTypes);
		}

		if (returnType == null)
			throw new NoSuchMethodException(String.format("No such method!className:%s, methodName:%s", type.getClassName(), methodName));

		return returnType;
	}

}

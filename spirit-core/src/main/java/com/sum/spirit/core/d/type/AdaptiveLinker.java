package com.sum.spirit.core.d.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

import cn.hutool.core.lang.Assert;

@Component
@Primary
public class AdaptiveLinker extends AbsAdaptiveLinker {

	public static final String ARRAY_LENGTH = "length";

	@Autowired
	public TypeFactory factory;

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(fieldName, "Field name cannot be empty!");
		// xxx.class class是关键字
		if (KeywordEnum.CLASS.value.equals(fieldName)) {
			return factory.create(TypeEnum.Class.value.getClassName(), type.getWrappedType());
		}
		// 原始类型没有属性和方法
		if (type.isPrimitive()) {
			throw new RuntimeException("The primitive type has no field!");
		}
		// 访问数组length直接返回int类型
		if (type.isArray() && ARRAY_LENGTH.equals(fieldName)) {
			return TypeEnum.int_t.value;
		}
		// 向上遍历推导
		IType returnType = getLinker(type).visitField(type, fieldName);
		if (returnType == null) {
			IType superType = getSuperType(type);
			if (superType != null) {
				return visitField(superType, fieldName);
			}
		}
		if (returnType == null) {
			throw new NoSuchFieldException(String.format("No such field!className:%s, fieldName:%s", type.getClassName(), fieldName));
		}
		return returnType;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(methodName, "Method name cannot be empty!");
		// super()和this()指代父类或者本身的构造函数，返回这个类本身
		if (KeywordEnum.SUPER.value.equals(methodName) || KeywordEnum.THIS.value.equals(methodName)) {
			return type;
		}
		// 原始类型没有属性和方法
		if (type.isPrimitive()) {
			throw new RuntimeException("The primitive type has no method!");
		}
		// 原始类型没有属性和方法
		if (type.isArray()) {
			throw new RuntimeException("Array has no method!");
		}
		// 向上遍历推导
		IType returnType = getLinker(type).visitMethod(type, methodName, parameterTypes);
		if (returnType == null) {
			IType superType = getSuperType(type);
			if (superType != null) {
				return visitMethod(superType, methodName, parameterTypes);
			}
		}
		if (returnType == null) {
			throw new NoSuchMethodException(String.format("No such method!className:%s, methodName:%s", type.getClassName(), methodName));
		}
		return returnType;
	}

}
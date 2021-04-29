package com.sum.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.StaticTypes;
import com.sum.spirit.core.compile.deduce.TypeDerivator;

import cn.hutool.core.lang.Assert;

@Primary
@Component
public class AdaptiveClassLinker implements ClassLinker {

	public static final String ARRAY_LENGTH = "length";

	@Autowired
	public TypeFactory factory;
	@Autowired
	public TypeDerivator derivator;
	@Autowired
	public Map<String, ClassLinker> linkers;

	public ClassLinker getLinker(IType type) {
		if (!type.isNative()) {
			return linkers.get("appClassLinker");
		} else {
			return linkers.get("nativeClassLinker");
		}
	}

	@Override
	public <T> T toClass(IType type) {
		return getLinker(type).toClass(type);
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return getLinker(type).getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		if (type.isPrimitive()) {
			return null;
		}
		if (type.isArray()) {
			return StaticTypes.OBJECT;
		}
		if (StaticTypes.OBJECT.equals(type)) {
			return null;
		}
		// 如果不存在父类，则返回Object
		IType superType = getLinker(type).getSuperType(type);
		if (superType == null) {
			return StaticTypes.OBJECT;
		}
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
		if (type.isPrimitive()) {
			return new ArrayList<>();
		}
		if (type.isArray()) {
			return new ArrayList<>();
		}
		return getLinker(type).getInterfaceTypes(type);
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.notNull(type, "Type cannot be null!");
		Assert.notEmpty(fieldName, "Field name cannot be empty!");
		// xxx.class class是关键字
		if (KeywordEnum.CLASS.value.equals(fieldName)) {
			return factory.create(StaticTypes.CLASS.getClassName(), derivator.toBox(type));
		}
		// 原始类型没有属性和方法
		if (type.isPrimitive()) {
			throw new RuntimeException("The primitive type has no field!");
		}
		// 访问数组length直接返回int类型
		if (type.isArray() && ARRAY_LENGTH.equals(fieldName)) {
			return StaticTypes.INT;
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
		// 如果已经推导到Object，并且方法名是empty的话，则直接返回布尔类型
		if (StaticTypes.OBJECT.equals(type) && KeywordEnum.EMPTY.value.equals(methodName)) {
			return StaticTypes.BOOLEAN;
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

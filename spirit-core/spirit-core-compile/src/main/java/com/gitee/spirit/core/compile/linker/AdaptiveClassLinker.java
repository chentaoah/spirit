package com.gitee.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.common.enums.ModifierEnum;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.TypeRegistry;

import cn.hutool.core.lang.Assert;

@Primary
@Component
public class AdaptiveClassLinker implements ClassLinker {

	public static final String ARRAY_LENGTH = "length";

	@Autowired
	public TypeFactory factory;
	@Autowired
	public Map<String, ClassLinker> linkers;

	public ClassLinker getLinker(IType type) {
		if (!type.isNative()) {
			return linkers.get("appClassLinker");
		} else {
			return linkers.get("extClassLinker");
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
			return TypeRegistry.OBJECT;
		}

		if (TypeRegistry.OBJECT.equals(type)) {
			return null;
		}

		IType superType = getLinker(type).getSuperType(type);// 如果不存在父类，则返回Object
		if (superType == null) {
			return TypeRegistry.OBJECT;
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

		if (KeywordEnum.CLASS.value.equals(fieldName)) {// xxx.class class是关键字
			return factory.create(TypeRegistry.CLASS.getClassName(), type.toBox());
		}

		if (type.isPrimitive()) {// 原始类型没有属性和方法
			throw new RuntimeException("The primitive type has no field!");
		}

		if (type.isArray() && ARRAY_LENGTH.equals(fieldName)) {// 访问数组length直接返回int类型
			return TypeRegistry.INT;
		}

		IType returnType = getLinker(type).visitField(type, fieldName);// 向上遍历推导
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

		if (KeywordEnum.SUPER.value.equals(methodName) || KeywordEnum.THIS.value.equals(methodName)) {// super()和this()指代父类或者本身的构造函数，返回这个类本身
			return type;
		}

		if (type.isPrimitive()) {// 原始类型没有属性和方法
			throw new RuntimeException("The primitive type has no method!");
		}

		if (type.isArray()) {// 原始类型没有属性和方法
			throw new RuntimeException("Array has no method!");
		}

		if (TypeRegistry.OBJECT.equals(type) && KeywordEnum.EMPTY.value.equals(methodName)) {// 如果已经推导到Object，并且方法名是empty的话，则直接返回布尔类型
			return TypeRegistry.BOOLEAN;
		}

		IType returnType = getLinker(type).visitMethod(type, methodName, parameterTypes);// 向上遍历推导
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

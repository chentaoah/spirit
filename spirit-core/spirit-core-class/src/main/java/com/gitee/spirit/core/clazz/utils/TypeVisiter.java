package com.gitee.spirit.core.clazz.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.gitee.spirit.core.clazz.entity.IType;
import com.google.common.base.Joiner;

import cn.hutool.core.lang.Assert;

public class TypeVisiter {

	public static IType forEachType(IType targetType, Consumer<IType> consumer) {
		Assert.notNull(targetType, "Target Type cannot be null!");
		// 拷贝一份
		IType newType = (IType) consumer.accept(TypeBuilder.copy(targetType));
		// 拷贝一份，注意不可修改集合
		List<IType> newGenericTypes = new ArrayList<>(newType.getGenericTypes());
		for (int index = 0; index < newGenericTypes.size(); index++) {
			IType genericType = forEachType(newGenericTypes.get(index), consumer);
			if (genericType != null) {
				newGenericTypes.set(index, genericType);
			}
		}
		newType.setGenericTypes(Collections.unmodifiableList(newGenericTypes));
		return newType;
	}

	public static IType forEachType(IType referType, IType targetType, Consumer0<IType> consumer) {
		Assert.notNull(targetType, "Target Type cannot be null!");
		// 拷贝一份
		IType newType = (IType) consumer.accept(referType, TypeBuilder.copy(targetType));
		// 参考的泛型参数
		List<IType> referGenericTypes = referType.getGenericTypes();
		// 拷贝一份，注意不可修改集合
		List<IType> newGenericTypes = new ArrayList<>(newType.getGenericTypes());
		for (int index = 0; index < newGenericTypes.size(); index++) {
			IType genericType = forEachType(referGenericTypes.get(index), newGenericTypes.get(index), consumer);
			if (genericType != null) {
				newGenericTypes.set(index, genericType);
			}
		}
		newType.setGenericTypes(Collections.unmodifiableList(newGenericTypes));
		return newType;
	}

	public static String forEachTypeName(IType targetType, Consumer<IType> consumer) {
		Assert.notNull(targetType, "Target Type cannot be null!");
		String typeName = (String) consumer.accept(targetType);
		if (targetType.isGenericType()) {
			List<String> typeNames = new ArrayList<>();
			for (IType genericType : targetType.getGenericTypes()) {
				typeNames.add(forEachTypeName(genericType, consumer));
			}
			typeName = typeName + "<" + Joiner.on(", ").join(typeNames) + ">";
		}
		return typeName;
	}

	public static interface Consumer<T> {
		Object accept(T t);
	}

	public static interface Consumer0<T> {
		Object accept(T t, T t1);
	}

}

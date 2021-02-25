package com.sum.spirit.core.clazz.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sum.spirit.core.clazz.entity.IType;

public class TypeVisiter {

	public static IType visit(IType targetType, Consumer<IType> consumer) {
		// 如果为空，不进行遍历
		if (targetType == null) {
			return targetType;
		}
		// 拷贝一份
		targetType = TypeBuilder.copy(targetType);
		targetType = (IType) consumer.accept(targetType);
		// 拷贝一份，注意不可修改集合
		List<IType> genericTypes = new ArrayList<>(targetType.getGenericTypes());
		for (int index = 0; index < genericTypes.size(); index++) {
			IType genericType = genericTypes.get(index);
			genericType = visit(genericType, consumer);
			if (genericType != null) {
				genericTypes.set(index, genericType);
			}
		}
		// 重置
		targetType.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return targetType;
	}

	public static IType visit(IType targetType, IType referType, Consumer0<IType> consumer) {
		// 如果为空，不进行遍历
		if (targetType == null) {
			return targetType;
		}
		// 拷贝一份
		targetType = TypeBuilder.copy(targetType);
		targetType = (IType) consumer.accept(targetType, referType);
		// 拷贝一份，注意不可修改集合
		List<IType> genericTypes = new ArrayList<>(targetType.getGenericTypes());
		for (int index = 0; index < genericTypes.size(); index++) {
			IType genericType = genericTypes.get(index);
			genericType = visit(genericType, referType.getGenericTypes().get(index), consumer);
			if (genericType != null) {
				genericTypes.set(index, genericType);
			}
		}
		// 重置
		targetType.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return targetType;
	}

	public static interface Consumer<T> {
		Object accept(T t);
	}

	public static interface Consumer0<T> {
		Object accept(T t, T t1);
	}

}

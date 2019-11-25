package com.sum.shy.core.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.api.Type;
import com.sum.shy.core.utils.ReflectUtils;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType extends AbsType {

	public Class<?> clazz;// 类名

	public List<NativeType> genericTypes;// List<E> E-String

	public NativeType(Class<?> clazz, List<NativeType> genericTypes) {
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(Class<?> clazz) {
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(Type type) {
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (Type genericType : type.getGenericTypes()) {
			genericTypes.add(new NativeType(genericType));
		}
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public String getTypeName() {
		return clazz.getSimpleName();
	}

	@Override
	public List<Type> getGenericTypes() {
		List<Type> list = new ArrayList<>();
		list.addAll(genericTypes);
		return list;
	}

	public Method findMethod(String methodName) {
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}

}

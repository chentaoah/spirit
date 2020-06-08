package com.sum.shy.member.deducer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.clazz.pojo.IType;
import com.sum.shy.core.pojo.StaticType;
import com.sum.shy.core.utils.ReflectUtils;

public class NativeLinker {

	public static IType visitField(IType type, String fieldName) {
		try {
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			Field field = clazz.getField(fieldName);
			return convertType(type, null, null, field.getGenericType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit field!fieldName:" + fieldName, e);
		}
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			Method method = findMethod(type, methodName, parameterTypes);
			Map<String, IType> namedTypes = getNamedTypes(type, method, parameterTypes);// 方法中因传入参数，而导致限定的泛型类型
			return convertType(type, namedTypes, null, method.getGenericReturnType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit method!methodName:" + methodName, e);
		}
	}

	public static Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = ReflectUtils.getClass(type.getClassName());
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				int index = 0;
				boolean flag = true;
				for (Parameter parameter : method.getParameters()) {
					IType parameterType = parameterTypes.get(index++);
					IType nativeParameterType = convertType(type, null, parameterType,
							parameter.getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag)
					return method;
			}
		}
		for (Method method : clazz.getMethods()) {// 这里要处理Object...这种形式
			if (method.getName().equals(methodName) && ReflectUtils.isIndefinite(method)) {
				Parameter[] parameters = method.getParameters();
				Parameter lastParameter = parameters[parameters.length - 1];
				boolean flag = true;
				for (int i = 0; i < parameters.length - 1; i++) {
					IType parameterType = parameterTypes.get(i);
					IType nativeParameterType = convertType(type, null, parameterType,
							parameters[i].getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag) {
					IType targetType = convertType(type, null, null, lastParameter.getParameterizedType())
							.getTargetType();
					for (int i = parameters.length - 1; i < parameterTypes.size(); i++) {
						IType parameterType = parameterTypes.get(i);
						if (!(targetType.isMatch(parameterType))) {
							flag = false;
							break;
						}
					}
				}
				if (flag)
					return method;
			}
		}
		throw new RuntimeException("The method was not found!method:" + methodName);
	}

	public static Map<String, IType> getNamedTypes(IType type, Method method, List<IType> parameterTypes) {
		Map<String, IType> namedTypes = new HashMap<>();
		int size = !ReflectUtils.isIndefinite(method) ? method.getParameterCount() : method.getParameterCount() - 1;
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < size; i++)
			convertType(type, namedTypes, parameterTypes.get(i), parameters[i].getParameterizedType());
		return namedTypes;
	}

	public static IType convertType(IType type, Map<String, IType> namedTypes, IType incomingType, Type nativeType) {

		if (nativeType instanceof Class) {// 一部分类型可以直接转换
			return TypeFactory.create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return StaticType.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// 泛型参数 E or K or V
			Class<?> clazz = ReflectUtils.getClass(type.getClassName());
			int index = getTypeVariableIndex(clazz, nativeType.toString());// 获取这个泛型名称在类中的index
			if (index >= 0) {
				return type.getGenericTypes().get(index);
			} else {
				if (namedTypes != null && namedTypes.containsKey(nativeType.toString())) {
					return namedTypes.get(nativeType.toString());
				} else {
					if (namedTypes != null)
						namedTypes.put(nativeType.toString(), incomingType);
					return incomingType;
				}
			}
		} else if (nativeType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			int index = 0;
			for (Type actualType : parameterizedType.getActualTypeArguments()) {
				IType genericType = incomingType != null ? incomingType.getGenericTypes().get(index++) : null;
				genericTypes.add(convertType(type, namedTypes, genericType, actualType));
			}
			return TypeFactory.create(clazz, genericTypes);
		}
		throw new RuntimeException("Convert native type failed!");
	}

	public static int getTypeVariableIndex(Class<?> clazz, String typeVariableName) {
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int i = 0; i < typeVariables.length; i++) {
			TypeVariable<?> typeVariable = typeVariables[i];
			if (typeVariable.toString().equals(typeVariableName))
				return i;
		}
		return -1;
	}

}

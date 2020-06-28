package com.sum.shy.core.link;

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

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.MemberLinker;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.utils.ReflectUtils;

public class NativeLinker implements MemberLinker {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public IType visitField(IType type, String fieldName) {
		try {
			Field field = type.toNativeClass().getField(fieldName);
			return convertType(type, null, null, field.getGenericType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit field!fieldName:" + fieldName, e);
		}
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			Method method = findMethod(type, methodName, parameterTypes);
			Map<String, IType> qualifyingTypes = getQualifyingTypes(type, method, parameterTypes);// 方法中因传入参数，而导致限定的泛型类型
			return convertType(type, qualifyingTypes, null, method.getGenericReturnType());

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit method!methodName:" + methodName, e);
		}
	}

	public Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		for (Method method : type.toNativeClass().getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int index = 0;
				for (Parameter parameter : method.getParameters()) {
					IType parameterType = parameterTypes.get(index++);
					IType nativeParameterType = convertType(type, null, parameterType, parameter.getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag)
					return method;
			}
		}

		Method method = findIndefiniteMethod(type, methodName, parameterTypes);
		if (method != null)
			return method;

		throw new RuntimeException("The method was not found!method:" + methodName);
	}

	public Method findIndefiniteMethod(IType type, String methodName, List<IType> parameterTypes) {
		// 处理不定项方法，Object... objects
		for (Method method : type.toNativeClass().getMethods()) {
			if (method.getName().equals(methodName) && ReflectUtils.isIndefinite(method)) {
				Parameter[] parameters = method.getParameters();
				boolean flag = true;
				for (int i = 0; i < parameters.length - 1; i++) {
					IType parameterType = parameterTypes.get(i);
					IType nativeParameterType = convertType(type, null, parameterType, parameters[i].getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag) {
					Parameter lastParameter = parameters[parameters.length - 1];
					IType targetType = convertType(type, null, null, lastParameter.getParameterizedType()).getTargetType();
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
		return null;
	}

	public Map<String, IType> getQualifyingTypes(IType type, Method method, List<IType> parameterTypes) {
		Map<String, IType> qualifyingTypes = new HashMap<>();
		int size = !ReflectUtils.isIndefinite(method) ? method.getParameterCount() : method.getParameterCount() - 1;
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < size; i++)
			convertType(type, qualifyingTypes, parameterTypes.get(i), parameters[i].getParameterizedType());
		return qualifyingTypes;
	}

	public IType convertType(IType type, Map<String, IType> qualifyingTypes, IType parameterType, Type nativeType) {

		if (nativeType instanceof Class) {// 一部分类型可以直接转换
			return factory.create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// 特指泛型中的Class<?>中的问号
			return StaticType.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// 泛型参数 E or K or V
			// 1.可能是类型定义的泛型参数
			int index = getTypeVariableIndex(type.toNativeClass(), nativeType.toString());
			if (index >= 0) {
				return type.getGenericTypes().get(index);
			} else {
				// 2.也可能是根据入参，导致返回类型限定的泛型参数
				if (qualifyingTypes != null && qualifyingTypes.containsKey(nativeType.toString())) {
					return qualifyingTypes.get(nativeType.toString());
				} else {
					// 放入限定类型的集合中，以便推导返回类型
					if (qualifyingTypes != null)
						qualifyingTypes.put(nativeType.toString(), parameterType);
					// 3.也可能是影射的入参中的泛型参数
					return parameterType;
				}
			}
		} else if (nativeType instanceof ParameterizedType) {// 泛型 List<E>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> clazz = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			int index = 0;
			for (Type actualType : parameterizedType.getActualTypeArguments()) {
				IType genericType = parameterType != null ? parameterType.getGenericTypes().get(index++) : null;
				genericTypes.add(convertType(type, qualifyingTypes, genericType, actualType));
			}
			return factory.create(clazz, genericTypes);
		}
		throw new RuntimeException("Convert native type failed!");
	}

	public int getTypeVariableIndex(Class<?> clazz, String typeVariableName) {
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int i = 0; i < typeVariables.length; i++) {
			TypeVariable<?> typeVariable = typeVariables[i];
			if (typeVariable.toString().equals(typeVariableName))
				return i;
		}
		return -1;
	}

}

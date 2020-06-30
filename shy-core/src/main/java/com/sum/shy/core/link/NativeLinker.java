package com.sum.shy.core.link;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.ClassLinker;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.utils.ReflectUtils;

public class NativeLinker implements ClassLinker {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		return (T) ReflectUtils.getClass(type.getClassName());// 可能是数组
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		Class<?> clazz = toClass(type);
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int i = 0; i < typeVariables.length; i++) {
			TypeVariable<?> typeVariable = typeVariables[i];
			if (typeVariable.toString().equals(genericName))
				return i;
		}
		return -1;
	}

	@Override
	public IType getSuperType(IType type) {
		Class<?> clazz = toClass(type);
		Type nativeSuperType = clazz.getGenericSuperclass();
		IType superType = nativeSuperType != null ? factory.create(nativeSuperType) : null;
		return factory.populateType(type, superType);
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		Class<?> clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (Type interfaceType : clazz.getGenericInterfaces())
			interfaceTypes.add(factory.populateType(type, factory.create(interfaceType)));
		return interfaceTypes;
	}

	@Override
	public IType visitField(IType type, String fieldName) {
		try {
			Class<?> clazz = toClass(type);
			Field field = clazz.getDeclaredField(fieldName);

			int modifiers = type.getModifiers();
			Assert.isTrue(modifiers != 0, "Modifiers for accessible members must be set!fieldName:" + fieldName);
			if (field != null && ReflectUtils.isMatch(field, modifiers))
				return populateType(type, null, null, field.getGenericType());

			IType superType = type.getSuperType();
			if (superType != null)
				return visitField(superType, fieldName);

			throw new RuntimeException("No matching field was found!fieldName:" + fieldName);

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit field!fieldName:" + fieldName, e);
		}
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		try {
			Method method = findMethod(type, methodName, parameterTypes);
			int modifiers = type.getModifiers();
			Assert.isTrue(modifiers != 0, "Modifiers for accessible members must be set!methodName:" + methodName);
			if (method != null && ReflectUtils.isMatch(method, modifiers)) {
				Map<String, IType> qualifyingTypes = getQualifyingTypes(type, method, parameterTypes);// 方法中因传入参数，而导致限定的泛型类型
				return populateType(type, qualifyingTypes, null, method.getGenericReturnType());
			}

			IType superType = type.getSuperType();
			if (superType != null)
				return visitMethod(superType, methodName, parameterTypes);

			throw new RuntimeException("No matching method was found!methodName:" + methodName);

		} catch (Exception e) {
			throw new RuntimeException("Failed to visit method!methodName:" + methodName, e);
		}
	}

	public Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = toClass(type);
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int index = 0;
				for (Parameter parameter : method.getParameters()) {
					IType parameterType = parameterTypes.get(index++);
					IType nativeParameterType = populateType(type, null, parameterType, parameter.getParameterizedType());
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

		return null;
	}

	public Method findIndefiniteMethod(IType type, String methodName, List<IType> parameterTypes) {
		// 处理不定项方法，Object... objects
		Class<?> clazz = toClass(type);
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && ReflectUtils.isIndefinite(method)) {
				Parameter[] parameters = method.getParameters();
				boolean flag = true;
				for (int i = 0; i < parameters.length - 1; i++) {
					IType parameterType = parameterTypes.get(i);
					IType nativeParameterType = populateType(type, null, parameterType, parameters[i].getParameterizedType());
					if (!(nativeParameterType.isMatch(parameterType))) {
						flag = false;
						break;
					}
				}
				if (flag) {
					Parameter lastParameter = parameters[parameters.length - 1];
					IType targetType = populateType(type, null, null, lastParameter.getParameterizedType()).getTargetType();
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
			populateType(type, qualifyingTypes, parameterTypes.get(i), parameters[i].getParameterizedType());
		return qualifyingTypes;
	}

	public IType populateType(IType type, Map<String, IType> qualifyingTypes, IType mappingType, Type nativeType) {
		return populateType(type, qualifyingTypes, mappingType, factory.create(nativeType));
	}

	public IType populateType(IType type, Map<String, IType> qualifyingTypes, IType mappingType, IType nativeType) {

		if (nativeType.isGenericType()) {// List<T>
			List<IType> genericTypes = new ArrayList<>();
			int index = 0;
			for (IType genericType : nativeType.getGenericTypes()) {
				IType genericMappingType = mappingType != null ? mappingType.getGenericTypes().get(index++) : null;
				genericTypes.add(populateType(type, qualifyingTypes, genericMappingType, genericType));
			}
			nativeType.setGenericTypes(Collections.unmodifiableList(genericTypes));

		} else if (nativeType.isTypeVariable()) {// T or K
			String genericName = nativeType.getGenericName();
			// 1.可能是类型定义的泛型参数
			int index = getTypeVariableIndex(type, genericName);
			if (index >= 0) {
				return type.getGenericTypes().get(index);
			} else {
				// 2.也可能是根据入参，导致返回类型限定的泛型参数
				if (qualifyingTypes != null && qualifyingTypes.containsKey(genericName)) {
					return qualifyingTypes.get(genericName);
				} else {
					// 放入限定类型的集合中，以便推导返回类型
					if (qualifyingTypes != null)
						qualifyingTypes.put(genericName, mappingType);
					// 3.也可能是影射的入参中的泛型参数
					return mappingType;
				}
			}
		}
		return nativeType;
	}

}

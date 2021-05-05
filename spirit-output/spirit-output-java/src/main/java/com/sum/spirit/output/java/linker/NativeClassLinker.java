package com.sum.spirit.output.java.linker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.utils.ListUtils;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.output.java.ExtClassLoader;
import com.sum.spirit.output.java.deduce.NativeTypeDerivator;
import com.sum.spirit.output.java.deduce.NativeTypeFactory;
import com.sum.spirit.output.java.utils.ReflectUtils;

import cn.hutool.core.lang.Assert;

@Component
@Order(-80)
public class NativeClassLinker implements ClassLinker {

	@Autowired
	public ExtClassLoader classLoader;
	@Autowired
	public NativeTypeFactory factory;
	@Autowired
	public NativeTypeDerivator derivator;
	@Autowired
	public NativeMethodMatcher matcher;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		return (T) classLoader.findClass(type.getClassName());// 可能是数组
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		Class<?> clazz = toClass(type);
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int index = 0; index < typeVariables.length; index++) {
			TypeVariable<?> typeVariable = typeVariables[index];
			if (typeVariable.toString().equals(genericName)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public IType getSuperType(IType type) {
		Class<?> clazz = toClass(type);
		Type nativeSuperType = clazz.getGenericSuperclass();
		IType superType = nativeSuperType != null ? factory.create(nativeSuperType) : null;
		return derivator.populate(type, superType);
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		Class<?> clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (Type interfaceType : clazz.getGenericInterfaces()) {
			interfaceTypes.add(derivator.populate(type, factory.create(interfaceType)));
		}
		return interfaceTypes;
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!fieldName:" + fieldName);
		Class<?> clazz = toClass(type);
		Field field = ReflectUtils.getDeclaredField(clazz, fieldName);
		if (field != null && ReflectUtils.isAccessible(field, type.getModifiers())) {
			return derivator.populate(type, factory.create(field.getGenericType()));
		}
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!methodName:" + methodName);
		Method method = findMethod(type, methodName, parameterTypes);
		if (method != null && ReflectUtils.isAccessible(method, type.getModifiers())) {
			Map<String, IType> qualifyingTypes = getQualifyingTypes(type, method, parameterTypes);
			return derivator.populateReturnType(type, qualifyingTypes, factory.create(method.getGenericReturnType()));
		}
		return null;
	}

	public Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = toClass(type);
		List<Method> methods = ListUtils.findAll(Arrays.asList(clazz.getDeclaredMethods()), method -> methodName.equals(method.getName()));
		return ListUtils.findOneByScore(methods, eachMethod -> matcher.getMethodScore(type, eachMethod, parameterTypes));
	}

	public Map<String, IType> getQualifyingTypes(IType type, Method method, List<IType> parameterTypes) {
		Map<String, IType> qualifyingTypes = new HashMap<>();
		int size = !ReflectUtils.isIndefinite(method) ? method.getParameterCount() : method.getParameterCount() - 1;
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < size; i++) {
			// 根据本地类型创建统一的类型
			derivator.populateQualifying(type, parameterTypes.get(i), factory.create(parameters[i].getParameterizedType()), qualifyingTypes);
		}
		return qualifyingTypes;
	}

}

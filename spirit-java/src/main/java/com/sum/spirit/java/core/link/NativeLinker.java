package com.sum.spirit.java.core.link;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.java.utils.ReflectUtils;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.Assert;

@Component
@Order(-80)
public class NativeLinker extends AbsNativeLinker {

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!fieldName:" + fieldName);
		Class<?> clazz = toClass(type);
		// 查找字段
		Field field = ReflectUtils.getDeclaredField(clazz, fieldName);
		// 判定访问权限
		if (field != null && ReflectUtils.isAccessible(field, type.getModifiers())) {
			// 填充类型
			return factory.populate(type, factory.create(field.getGenericType()));
		}
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!methodName:" + methodName);
		// 首先从一般方法查找
		Method method = findMethod(type, methodName, parameterTypes);
		// 如果找不到，则从不定项方法中查找
		if (method == null) {
			method = findIndefiniteMethod(type, methodName, parameterTypes);
		}
		// 判定访问权限
		if (method != null && ReflectUtils.isAccessible(method, type.getModifiers())) {
			// 方法中因传入参数，而导致限定的泛型类型
			Map<String, IType> qualifyingTypes = getQualifyingTypes(type, method, parameterTypes);
			// 使用类型和限定类型进行填充
			return factory.populate(type, qualifyingTypes, factory.create(method.getGenericReturnType()));
		}
		return null;
	}

	public Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = toClass(type);
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int index = 0;
				for (Parameter parameter : method.getParameters()) {
					IType parameterType = parameterTypes.get(index++);
					IType nativeParameterType = factory.populate(type, parameterType, factory.create(parameter.getParameterizedType()));
					if (!isMoreAbstract(nativeParameterType, parameterType)) {
						flag = false;
						break;
					}
				}
				if (flag) {
					return method;
				}
			}
		}
		return null;
	}

	public Method findIndefiniteMethod(IType type, String methodName, List<IType> parameterTypes) {
		// 处理不定项方法，Object... objects
		Class<?> clazz = toClass(type);
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getName().equals(methodName) && ReflectUtils.isIndefinite(method)) {
				Parameter[] parameters = method.getParameters();
				boolean flag = true;
				// 先判断前面的参数是否一致
				for (int i = 0; i < parameters.length - 1; i++) {
					IType parameterType = parameterTypes.get(i);
					IType nativeParameterType = factory.populate(type, parameterType, factory.create(parameters[i].getParameterizedType()));
					if (!isMoreAbstract(nativeParameterType, parameterType)) {
						flag = false;
						break;
					}
				}
				// 如果前面的参数一致，判断后面的不定项参数是否一致
				if (flag) {
					Parameter lastParameter = parameters[parameters.length - 1];
					IType nativeParameterType = factory.create(lastParameter.getParameterizedType()).getTargetType();
					for (int i = parameters.length - 1; i < parameterTypes.size(); i++) {
						IType parameterType = parameterTypes.get(i);
						nativeParameterType = factory.populate(type, parameterType, nativeParameterType);
						if (!isMoreAbstract(nativeParameterType, parameterType)) {
							flag = false;
							break;
						}
					}
				}
				if (flag) {
					return method;
				}
			}
		}
		return null;
	}

	public Map<String, IType> getQualifyingTypes(IType type, Method method, List<IType> parameterTypes) {
		Map<String, IType> qualifyingTypes = new HashMap<>();
		int size = !ReflectUtils.isIndefinite(method) ? method.getParameterCount() : method.getParameterCount() - 1;
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < size; i++) {
			// 根据本地类型创建统一的类型
			factory.populate(type, parameterTypes.get(i), factory.create(parameters[i].getParameterizedType()), qualifyingTypes);
		}
		return qualifyingTypes;
	}

}

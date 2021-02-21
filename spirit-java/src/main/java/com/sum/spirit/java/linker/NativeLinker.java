package com.sum.spirit.java.linker;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.java.utils.ReflectUtils;

import cn.hutool.core.lang.Assert;

@Component
@Order(-80)
public class NativeLinker extends AbstractNativeLinker {

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!fieldName:" + fieldName);
		Class<?> clazz = toClass(type);
		Field field = ReflectUtils.getDeclaredField(clazz, fieldName);
		if (field != null && ReflectUtils.isAccessible(field, type.getModifiers())) {
			return factory.populate(type, factory.create(field.getGenericType()));
		}
		return null;
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		Assert.isTrue(type.getModifiers() != 0, "Modifiers for accessible members must be set!methodName:" + methodName);
		Method method = findMethod(type, methodName, parameterTypes);
		if (method != null && ReflectUtils.isAccessible(method, type.getModifiers())) {
			Map<String, IType> qualifyingTypes = getQualifyingTypes(type, method, parameterTypes);
			return factory.populate(type, qualifyingTypes, factory.create(method.getGenericReturnType()));
		}
		return null;
	}

	public Method findMethod(IType type, String methodName, List<IType> parameterTypes) {
		Class<?> clazz = toClass(type);
		for (Method method : clazz.getDeclaredMethods()) {
			// 方法名相同,并且参数个数相同或为不定项方法
			boolean flag = false;
			if (method.getName().equals(methodName)) {
				if (!ReflectUtils.isIndefinite(method) && parameterTypes.size() == method.getParameterCount()) {
					flag = true;
				} else if (ReflectUtils.isIndefinite(method) && parameterTypes.size() >= method.getParameterCount() - 1) {
					flag = true;
				}
			}
			// 如果不满足上一条件，直接下一个方法
			if (!flag) {
				continue;
			}
			// 遍历传入的方法
			for (int index = 0; index < parameterTypes.size(); index++) {
				IType parameterType = parameterTypes.get(index);
				// 保证索引不会溢出
				int idx = index < method.getParameterCount() - 1 ? index : method.getParameterCount() - 1;
				// 分为两种情况，一种是最后一个参数之前的，一种是最后一个参数
				Parameter parameter = method.getParameters()[idx];
				// 获取本地参数类型
				IType nativeParameterType = factory.create(parameter.getParameterizedType());
				// 如果最后一个参数，而且是不定项参数，则取数组里的类型
				if (idx == method.getParameterCount() - 1 && ReflectUtils.isIndefinite(parameter)) {
					nativeParameterType = nativeParameterType.getTargetType();
				}
				// 填充类型里的泛型参数
				nativeParameterType = factory.populate(type, parameterType, nativeParameterType);
				if (!isMoreAbstract(nativeParameterType, parameterType)) {
					flag = false;
					break;
				}
			}
			if (flag) {
				return method;
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

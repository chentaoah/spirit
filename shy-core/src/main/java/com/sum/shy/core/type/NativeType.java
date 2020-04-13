package com.sum.shy.core.type;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.type.api.AbsType;
import com.sum.shy.core.utils.ReflectUtils;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType extends AbsType {

	public Class<?> clazz;// 类名

	public NativeType(Class<?> clazz) {
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(Class<?> clazz, List<IType> genericTypes) {
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(IType type) {
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (IType genericType : type.getGenericTypes())
			genericTypes.add(new NativeType(genericType));
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	@Override
	public String getSuperName() {
		return clazz.getSuperclass().getName();
	}

	@Override
	public IType getSuper() {
		return new NativeType(clazz.getSuperclass());
	}

	@Override
	public List<String> getInterfaceNames() {
		List<String> interfaceNames = new ArrayList<>();
		for (Class<?> interfaceClass : clazz.getInterfaces())
			interfaceNames.add(interfaceClass.getName());
		return interfaceNames;
	}

	@Override
	public List<IType> getInterfaces() {
		List<IType> interfaces = new ArrayList<>();
		for (Class<?> interfaceClass : clazz.getInterfaces())
			interfaces.add(new NativeType(interfaceClass));
		return interfaces;
	}

	public Method findMethod(String methodName, List<IType> parameterTypes) {
		// 这里要处理Object...这种形式
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == parameterTypes.size()) {
				boolean flag = true;
				int count = 0;
//				for (Parameter parameter : method.getParameters()) {
//					NativeType nativeType = (NativeType) NativeVisiter.visitMember(super.clazz, this,
//							parameter.getParameterizedType());
//					IType type = parameterTypes.get(count++);
//					Class<?> clazz = ReflectUtils.getClass(type.getClassName());
//					if (!(clazz.isAssignableFrom(nativeType.clazz))) {
//						flag = false;
//						break;
//					}
//				}
				if (flag)
					return method;
			}
		}
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		throw new RuntimeException("The method was not found!method:" + methodName);
	}

}

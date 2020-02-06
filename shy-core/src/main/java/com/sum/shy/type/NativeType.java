package com.sum.shy.type;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.clazz.IClass;
import com.sum.shy.type.api.AbsType;
import com.sum.shy.type.api.IType;
import com.sum.shy.utils.ReflectUtils;
import com.sum.shy.visiter.NativeVisiter;

/**
 * 本地类型
 * 
 * @author chentao
 *
 */
public class NativeType extends AbsType {

	public Class<?> clazz;// 类名

	public NativeType(IClass iClass, Class<?> clazz, List<IType> genericTypes) {
		super(iClass);
		this.clazz = clazz;
		this.genericTypes = genericTypes == null ? new ArrayList<>() : genericTypes;
	}

	public NativeType(IClass iClass, Class<?> clazz) {
		super(iClass);
		this.clazz = clazz;
		this.genericTypes = new ArrayList<>();
	}

	public NativeType(IClass iClass, IType type) {
		super(iClass);
		this.clazz = ReflectUtils.getClass(type.getClassName());
		this.genericTypes = new ArrayList<>();
		for (IType genericType : type.getGenericTypes()) {
			genericTypes.add(new NativeType(iClass, genericType));
		}
	}

	@Override
	public String getClassName() {
		return clazz.getName();
	}

	@Override
	public String getSimpleName() {
		return clazz.getSimpleName();
	}

	public Method findMethod(String methodName, List<IType> paramTypes) {

		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && method.getParameterCount() == paramTypes.size()) {
				boolean flag = true;
				int count = 0;
				for (Parameter parameter : method.getParameters()) {
					NativeType nativeType = (NativeType) NativeVisiter.visitMember(super.clazz, this,
							parameter.getParameterizedType());
					IType type = paramTypes.get(count++);
					Class<?> clazz = ReflectUtils.getClass(type.getClassName());
					if (!(clazz.isAssignableFrom(nativeType.clazz))) {
						flag = false;
						break;
					}
				}
				if (flag)
					return method;
			}
		}
		throw new RuntimeException("The method was not found!method:" + methodName);
	}

}

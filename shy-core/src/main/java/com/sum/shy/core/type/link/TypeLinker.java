package com.sum.shy.core.type.link;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.type.IType;
import com.sum.shy.core.type.TypeFactory;
import com.sum.shy.core.utils.ReflectUtils;

public class TypeLinker {

	public static IType visitField(IType type, String fieldName) {
		return !type.isNative() ? CodeLinker.visitField(type, fieldName) : NativeLinker.visitField(type, fieldName);
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		return !type.isNative() ? CodeLinker.visitMethod(type, methodName, parameterTypes)
				: visitMethod(type, methodName, parameterTypes);
	}

	public static boolean isAssignableFrom(IType father, IType type) {
		// 如果两个className相同，则直接返回
		if (father.equals(type))
			return true;

		if (!type.isNative()) {
			IClass clazz = Context.get().findClass(type.getClassName());
			// 1.判断父类和接口是否是
			String superName = clazz.getSuperName();
			if (father.getClassName().equals(superName))
				return true;
			List<String> interfaces = clazz.getInterfaces();
			for (String inter : interfaces) {
				if (father.getClassName().equals(inter))
					return true;
			}
			// 2.向上递归
			boolean flag = isAssignableFrom(father, TypeFactory.create(superName));
			if (!flag) {
				for (String inter : interfaces) {
					flag = isAssignableFrom(father, TypeFactory.create(inter));
					if (flag)
						break;
				}
			}
			return flag;

		} else {
			if (father.isNative()) {// 按照编译规则，Native类是不可能够访问到未曾编译的代码
				Class<?> fatherClass = ReflectUtils.getClass(father.getClassName());
				Class<?> clazz = ReflectUtils.getClass(type.getClassName());
				return fatherClass.isAssignableFrom(clazz);
			}
		}
		return false;

	}

}

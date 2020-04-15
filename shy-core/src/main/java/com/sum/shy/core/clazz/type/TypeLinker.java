package com.sum.shy.core.clazz.type;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.utils.ReflectUtils;

public class TypeLinker {

	public static IType visitField(IType type, String fieldName) {

		if (Constants.CLASS_KEYWORD.equals(fieldName))
			return StaticType.CLASS_TYPE;// xxx.class class是关键字

		if (type.isArray() && Constants.ARRAY_LENGTH.equals(fieldName))
			return StaticType.INT_TYPE;// 访问数组length直接返回int类型

		return !type.isNative() ? CodeLinker.visitField(type, fieldName) : NativeLinker.visitField(type, fieldName);
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

		if (Constants.SUPER_KEYWORD.equals(methodName) || Constants.THIS_KEYWORD.equals(methodName))
			return type;// super()和this()指代父类或者本身的构造函数，返回这个类本身

		return !type.isNative() ? CodeLinker.visitMethod(type, methodName, parameterTypes)
				: visitMethod(type, methodName, parameterTypes);
	}

	public static boolean isAssignableFrom(IType father, IType child) {
		// 如果两个className相同，则直接返回
		if (father.equals(child))
			return true;

		// 如果任一是数组,则直接退出,借用了上面的条件
		if (father.isArray() || child.isArray())
			return false;

		// 逻辑到这里，可以确定两个都不是数组
		if (!child.isNative()) {
			IClass clazz = Context.get().findClass(child.getTargetName());
			// 1.判断父类和接口是否是
			String superName = clazz.getSuperName();
			if (father.getTargetName().equals(superName))
				return true;
			List<String> interfaces = clazz.getInterfaces();
			for (String inter : interfaces) {
				if (father.getTargetName().equals(inter))
					return true;
			}
			// 2.向上递归
			boolean flag = isAssignableFrom(father, TypeFactory.createType(clazz, superName));
			if (!flag) {
				for (String inter : interfaces) {
					flag = isAssignableFrom(father, TypeFactory.createType(clazz, inter));
					if (flag)
						break;
				}
			}
			return flag;

		} else {
			if (father.isNative()) {// 按照编译规则，Native类是不可能够访问到未曾编译的代码
				Class<?> fatherClass = ReflectUtils.getClass(father.getClassName());
				Class<?> childClass = ReflectUtils.getClass(child.getClassName());
				return fatherClass.isAssignableFrom(childClass);
			}
		}
		return false;

	}

}

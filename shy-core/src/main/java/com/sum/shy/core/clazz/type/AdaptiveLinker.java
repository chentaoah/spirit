package com.sum.shy.core.clazz.type;

import java.util.List;

import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.StaticType;
import com.sum.shy.lib.Assert;

public class AdaptiveLinker {

	public static IType visitField(IType type, String fieldName) {

		if (type == null)
			return null;

		Assert.notEmpty(fieldName, "Field name cannot be empty!");

		if (Constants.CLASS_KEYWORD.equals(fieldName)) {// xxx.class class是关键字
			IType returnType = TypeFactory.create(Class.class);
			returnType.getGenericTypes().add(type);
			return returnType;
		}

		if (type.isArray() && Constants.ARRAY_LENGTH.equals(fieldName))
			return StaticType.INT_TYPE;// 访问数组length直接返回int类型

		return !type.isNative() ? CodeLinker.visitField(type, fieldName) : NativeLinker.visitField(type, fieldName);
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

		if (type == null)
			return null;

		Assert.notEmpty(methodName, "Method name cannot be empty!");

		if (Constants.SUPER_KEYWORD.equals(methodName) || Constants.THIS_KEYWORD.equals(methodName))
			return type;// super()和this()指代父类或者本身的构造函数，返回这个类本身

		return !type.isNative() ? CodeLinker.visitMethod(type, methodName, parameterTypes) : NativeLinker.visitMethod(type, methodName, parameterTypes);
	}

}

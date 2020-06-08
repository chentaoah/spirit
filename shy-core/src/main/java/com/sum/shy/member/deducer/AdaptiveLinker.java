package com.sum.shy.member.deducer;

import java.util.List;

import com.sum.shy.clazz.pojo.IType;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.Constants;
import com.sum.shy.pojo.StaticType;

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

		if (type.isObj())
			return null;

		return !type.isNative() ? CodeLinker.visitField(type, fieldName) : NativeLinker.visitField(type, fieldName);
	}

	public static IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

		if (type == null)
			return null;

		Assert.notEmpty(methodName, "Method name cannot be empty!");

		if (Constants.SUPER_KEYWORD.equals(methodName) || Constants.THIS_KEYWORD.equals(methodName))
			return type;// super()和this()指代父类或者本身的构造函数，返回这个类本身

		if (type.isObj()) {// 如果是Object类型，则直接返回了
			if (Constants.OBJECT_EQUALS.equals(methodName)) {
				return StaticType.BOOLEAN_TYPE;

			} else if (Constants.OBJECT_TO_STRING.equals(methodName)) {
				return StaticType.STRING_TYPE;
			}
		}

		return !type.isNative() ? CodeLinker.visitMethod(type, methodName, parameterTypes)
				: NativeLinker.visitMethod(type, methodName, parameterTypes);
	}

}

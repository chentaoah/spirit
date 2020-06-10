package com.sum.shy.api.service.deducer;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deducer.MemberLinker;
import com.sum.shy.api.deducer.TypeFactory;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.Constants;
import com.sum.shy.common.StaticType;
import com.sum.shy.lib.Assert;
import com.sum.shy.linker.CodeLinker;
import com.sum.shy.linker.NativeLinker;

public class MemberLinkerImpl implements MemberLinker {

	public TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	@Override
	public IType visitField(IType type, String fieldName) {

		if (type == null)
			return null;

		Assert.notEmpty(fieldName, "Field name cannot be empty!");

		if (Constants.CLASS_KEYWORD.equals(fieldName)) {// xxx.class class是关键字
			IType returnType = factory.create(Class.class);
			returnType.getGenericTypes().add(type);
			return returnType;
		}

		if (type.isArray() && Constants.ARRAY_LENGTH.equals(fieldName))
			return StaticType.INT_TYPE;// 访问数组length直接返回int类型

		if (type.isObj())
			return null;

		return !type.isNative() ? CodeLinker.visitField(type, fieldName) : NativeLinker.visitField(type, fieldName);
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

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

		return !type.isNative() ? CodeLinker.visitMethod(type, methodName, parameterTypes) : NativeLinker.visitMethod(type, methodName, parameterTypes);
	}

}

package com.sum.shy.core.link;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.deduce.TypeFactory;
import com.sum.shy.api.link.MemberLinker;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.common.StaticType;

public class AdaptiveLinker implements MemberLinker {

	public static TypeFactory factory = ProxyFactory.get(TypeFactory.class);

	public static MemberLinker codeLinker = ProxyFactory.cast(MemberLinker.class, "code_linker");

	public static MemberLinker nativeLinker = ProxyFactory.cast(MemberLinker.class, "native_linker");

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return !type.isNative() ? codeLinker.getTypeVariableIndex(type, genericName) : nativeLinker.getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType visitField(IType type, String fieldName) {

		if (type == null)
			return null;

		Assert.notEmpty(fieldName, "Field name cannot be empty!");

		// xxx.class class是关键字
		if (Constants.CLASS_KEYWORD.equals(fieldName))
			return factory.create(Class.class, type);

		if (type.isArray() && Constants.ARRAY_LENGTH.equals(fieldName))
			return StaticType.INT_TYPE;// 访问数组length直接返回int类型

		if (type.isObj())
			return null;

		return !type.isNative() ? codeLinker.visitField(type, fieldName) : nativeLinker.visitField(type, fieldName);
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

		return !type.isNative() ? codeLinker.visitMethod(type, methodName, parameterTypes) : nativeLinker.visitMethod(type, methodName, parameterTypes);
	}

}

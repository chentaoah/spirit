package com.sum.shy.core.link;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.link.MemberLinker;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IType;

public class CodeLinker implements MemberLinker {

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static MemberLinker linker = ProxyFactory.get(MemberLinker.class);

	@Override
	public IType visitField(IType type, String fieldName) {

		IClass clazz = type.toIClass();
		if (clazz.existField(fieldName)) {
			IField field = clazz.getField(fieldName);
			IType returnType = visiter.visitMember(clazz, field);
			return convertType(type, returnType);

		} else {
			return linker.visitField(clazz.getSuperType(), fieldName);
		}
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {

		IClass clazz = type.toIClass();
		IMethod method = clazz.getMethod(type, methodName, parameterTypes);
		if (method != null) {
			IType returnType = visiter.visitMember(clazz, method);
			return convertType(type, returnType);
		}
		return linker.visitMethod(clazz.getSuperType(), methodName, parameterTypes);
	}

	public static IType convertType(IType type, IType returnType) {
		// If it is a generic parameter, it is derived by the type passed in
		if (returnType.isTypeVariable()) {
			int index = type.toIClass().getTypeVariableIndex(returnType.getGenericName());
			Assert.isTrue(index >= 0, "Index of type variable less than 0!");
			Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
			List<IType> genericTypes = type.getGenericTypes();
			return genericTypes.get(index);
		}
		return returnType;
	}

}

package com.sum.shy.core.link;

import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.link.MemberLinker;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Context;

public class CodeLinker implements MemberLinker {

	public static MemberLinker linker = ProxyFactory.get(MemberLinker.class);

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	@Override
	public IType visitField(IType type, String fieldName) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existField(fieldName)) {
			IField field = clazz.getField(fieldName);
			return visiter.visitMember(clazz, field);

		} else {
			return linker.visitField(clazz.getSuperType(), fieldName);
		}
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
		String className = type.getClassName();
		IClass clazz = Context.get().findClass(className);
		if (clazz.existMethod(methodName, parameterTypes)) {
			IMethod method = clazz.getMethod(methodName, parameterTypes);
			return visiter.visitMember(clazz, method);

		} else {
			return linker.visitMethod(clazz.getSuperType(), methodName, parameterTypes);
		}
	}
}

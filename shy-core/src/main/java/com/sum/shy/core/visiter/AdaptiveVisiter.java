package com.sum.shy.core.visiter;

import java.util.List;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.visiter.api.Visiter;

public class AdaptiveVisiter implements Visiter {

	public Visiter codeVisiter = new CodeVisiter();

	public Visiter nativeVisiter = new NativeVisiter();

	@Override
	public IType visitField(IClass clazz, IType type, String fieldName) {
		return choiceVisiter(type).visitField(clazz, type, fieldName);
	}

	@Override
	public IType visitMethod(IClass clazz, IType type, String methodName, List<IType> parameterTypes) {
		return choiceVisiter(type).visitMethod(clazz, type, methodName, parameterTypes);
	}

	public Visiter choiceVisiter(IType type) {
		return Context.get().contains(type.getClassName()) ? codeVisiter : nativeVisiter;
	}

}

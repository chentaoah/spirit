package com.gitee.spirit.core.compile.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.VisitContext;

import cn.hutool.core.lang.Assert;

@Component
public class VariableTracker {

	@Autowired
	public TypeDerivator derivator;
	@Autowired
	public ClassLinker linker;

	public IType findTypeByKeyword(IClass clazz, String variableName) {
		if (KeywordEnum.isSuper(variableName)) {// super
			return derivator.withSuperModifiers(clazz.getSuperType());

		} else if (KeywordEnum.isThis(variableName)) {// this
			return derivator.withThisModifiers(clazz.getType());
		}
		throw new RuntimeException("Variable must be declared!variableName:" + variableName);
	}

	public IType findTypeByContext(VisitContext context, String variableName) {
		if (context != null && context.isMethodScope()) {
			// 变量
			for (IVariable variable : context.variables) {
				if (variable.getName().equals(variableName) && context.getBlockId().startsWith(variable.blockId)) {
					return variable.getType();
				}
			}
			// 方法入参
			IMethod method = (IMethod) context.member;
			for (IParameter parameter : method.parameters) {
				if (parameter.getName().equals(variableName)) {
					return parameter.getType();
				}
			}
		}
		return null;
	}

	public IType findTypeByInherit(IClass clazz, String variableName) {
		try {
			// 从本身和父类里面寻找，父类可能是native的
			return linker.visitField(derivator.withThisModifiers(clazz.getType()), variableName);

		} catch (NoSuchFieldException e) {
			return null;
		}
	}

	public IType findVariableType(IClass clazz, VisitContext context, String variableName) {
		IType type = findTypeByContext(context, variableName);
		if (type == null) {
			type = findTypeByInherit(clazz, variableName);
		}
		return type;
	}

	public IType getVariableType(IClass clazz, VisitContext context, String variableName) {
		IType type = findVariableType(clazz, context, variableName);
		Assert.notNull(type, "Variable must be declared!variableName:" + variableName);
		return type;
	}

}

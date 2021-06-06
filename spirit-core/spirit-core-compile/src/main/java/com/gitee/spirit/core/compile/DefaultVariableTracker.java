package com.gitee.spirit.core.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.VariableTracker;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.VisitContext;

@Component
public class DefaultVariableTracker implements VariableTracker {

	@Autowired
	public AppTypeDerivator derivator;
	@Autowired
	public ClassLinker linker;

	@Override
	public IType findVariableType(VisitContext context, String variableName) {
		IType type = findTypeByKeyword(context, variableName);
		if (type == null) {
			type = context.isMethodScope() ? findTypeByContext(context, variableName) : null;
		}
		if (type == null) {
			type = findTypeByInherit(context, variableName);
		}
		return type;
	}

	public IType findTypeByKeyword(VisitContext context, String variableName) {
		IClass clazz = context.clazz;
		if (KeywordEnum.isSuper(variableName)) {// super
			return derivator.withSuperModifiers(clazz.getSuperType());

		} else if (KeywordEnum.isThis(variableName)) {// this
			return derivator.withThisModifiers(clazz.getType());
		}
		return null;
	}

	public IType findTypeByContext(VisitContext context, String variableName) {
		for (IVariable variable : context.variables) {
			if (variable.getName().equals(variableName) && context.getBlockId().startsWith(variable.blockId)) {
				return variable.getType();
			}
		}
		IMethod method = (IMethod) context.member;
		for (IParameter parameter : method.parameters) {
			if (parameter.getName().equals(variableName)) {
				return parameter.getType();
			}
		}
		return null;
	}

	public IType findTypeByInherit(VisitContext context, String variableName) {
		try {
			return linker.visitField(derivator.withThisModifiers(context.clazz.getType()), variableName);// 从本身和父类里面寻找，父类可能是native的
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}

package com.gitee.spirit.core.compile.derivator;

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
	public ClassLinker linker;

	@Override
	public IType findVariableType(VisitContext context, String variableName) {
		IType type = findTypeByKeyword(context, variableName);
		type = type == null ? findTypeByContext(context, variableName) : type;
		type = type == null ? findTypeByInherit(context, variableName) : type;
		return type;
	}

	public IType findTypeByKeyword(VisitContext context, String variableName) {
		IClass clazz = context.clazz;
		if (KeywordEnum.isSuper(variableName)) {
			return clazz.getSuperType().withProtected();
		} else if (KeywordEnum.isThis(variableName)) {
			return clazz.getType().withPrivate();
		}
		return null;
	}

	public IType findTypeByContext(VisitContext context, String variableName) {
		if (!context.isMethodScope()) {
			return null;
		}
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
			// 从本身和父类里面寻找，父类可能是native的
			return linker.visitField(context.clazz.getType().withPrivate(), variableName);
		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}

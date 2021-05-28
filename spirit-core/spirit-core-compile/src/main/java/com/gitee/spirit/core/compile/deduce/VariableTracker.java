package com.gitee.spirit.core.compile.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Attribute;
import com.gitee.spirit.common.enums.KeywordEnum;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.compile.entity.MethodContext;
import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
public class VariableTracker {

	@Autowired
	public TypeDerivator derivator;
	@Autowired
	public ClassLinker linker;

	public void visit(IClass clazz, MethodContext context, Statement statement) {
		StmtVisiter.visit(statement, stmt -> {
			stmt.forEach(token -> {
				if (token.attr(Attribute.TYPE) != null) {
					return;
				}
				if (token.isVariable()) {// variable
					String variableName = token.toString();
					IType type = getVariableType(clazz, context, variableName);
					token.setAttr(Attribute.TYPE, type);

				} else if (token.isKeyword() && KeywordEnum.isKeywordVariable(token.getValue())) {
					String variableName = token.toString();
					IType type = findTypeByKeyword(clazz, variableName);
					token.setAttr(Attribute.TYPE, type);
				}
			});
		});
	}

	public IType findTypeByKeyword(IClass clazz, String variableName) {
		if (KeywordEnum.isSuper(variableName)) {// super
			return derivator.withSuperModifiers(clazz.getSuperType());

		} else if (KeywordEnum.isThis(variableName)) {// this
			return derivator.withThisModifiers(clazz.getType());
		}
		throw new RuntimeException("Variable must be declared!variableName:" + variableName);
	}

	public IType findTypeByContext(MethodContext context, String variableName) {
		if (context != null) {
			for (IVariable variable : context.variables) {// 变量
				if (variable.getName().equals(variableName) && context.getBlockId().startsWith(variable.blockId)) {
					return variable.getType();
				}
			}
			for (IParameter parameter : context.method.parameters) {// 方法入参
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

	public IType findVariableType(IClass clazz, MethodContext context, String variableName) {
		IType type = findTypeByContext(context, variableName);
		if (type == null) {
			type = findTypeByInherit(clazz, variableName);
		}
		return type;
	}

	public IType getVariableType(IClass clazz, MethodContext context, String variableName) {
		IType type = findVariableType(clazz, context, variableName);
		Assert.notNull(type, "Variable must be declared!variableName:" + variableName);
		return type;
	}

}

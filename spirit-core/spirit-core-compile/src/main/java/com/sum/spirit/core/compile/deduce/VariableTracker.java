package com.sum.spirit.core.compile.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IParameter;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.entity.IVariable;
import com.sum.spirit.core.compile.ClassVisiter;
import com.sum.spirit.core.compile.entity.MethodContext;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
public class VariableTracker {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactoryImpl factory;
	@Autowired
	public TypeDerivator derivator;

	public void visit(IClass clazz, MethodContext context, Statement statement) {
		StmtVisiter.visit(statement, stmt -> {
			stmt.forEach(token -> {
				if (token.attr(AttributeEnum.TYPE) != null) {
					return;
				}
				if (token.isVariable()) {// variable
					String variableName = token.toString();
					IType type = getVariableType(clazz, context, variableName);
					token.setAttr(AttributeEnum.TYPE, type);

				} else if (token.isKeyword() && KeywordEnum.isKeywordVariable(token.getValue())) {
					String variableName = token.toString();
					IType type = findTypeByKeyword(clazz, variableName);
					token.setAttr(AttributeEnum.TYPE, type);
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

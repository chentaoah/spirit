package com.sum.spirit.core.c.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.d.type.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
public class VariableTracker {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	public void track(IClass clazz, MethodContext context, Statement statement) {
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			if (currentToken.attr(AttributeEnum.TYPE) != null) {
				return null;
			}
			if (currentToken.isVar()) {
				String name = currentToken.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isArrayIndex()) {// .strs[0]
				String name = currentToken.attr(AttributeEnum.MEMBER_NAME);
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				// 转换数组类型为目标类型
				type = type.getTargetType();
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isKeyword() && (KeywordEnum.SUPER.value.equals(currentToken.value) || KeywordEnum.THIS.value.equals(currentToken.value))) {
				String name = currentToken.toString();
				IType type = findType(clazz, context, name);
				Assert.notNull(type, "Variable must be declared!name:" + name);
				currentToken.setAttr(AttributeEnum.TYPE, type);
			}
			return null;
		});
	}

	public IType findType(IClass clazz, MethodContext context, String variableName) {
		// super
		if (KeywordEnum.SUPER.value.equals(variableName)) {
			return clazz.getSuperType().toSuper();
		}
		// this
		if (KeywordEnum.THIS.value.equals(variableName)) {
			return clazz.getType().toThis();
		}
		if (context != null) {
			for (IVariable variable : context.variables) {// 变量
				if (variable.getName().equals(variableName) && context.getBlockId().startsWith(variable.blockId)) {
					return variable.getType();
				}
			}
			for (IParameter parameter : context.method.parameters) {// 入参
				if (parameter.getName().equals(variableName)) {
					return parameter.getType();
				}
			}
		}
		// 从本身和父类里面寻找，父类可能是native的
		try {
			return linker.visitField(clazz.getType().toThis(), variableName);

		} catch (NoSuchFieldException e) {
			return null;
		}
	}

}

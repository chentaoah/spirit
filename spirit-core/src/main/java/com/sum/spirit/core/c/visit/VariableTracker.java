package com.sum.spirit.core.c.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.api.ElementAction;
import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.d.type.TypeFactory;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IParameter;
import com.sum.spirit.pojo.clazz.IVariable;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
@Order(-60)
public class VariableTracker implements ElementAction {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	public boolean isTrigger(ElementEvent event) {
		return event.element != null || event.statement != null;
	}

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		MethodContext context = event.context;
		Statement statement = event.element != null ? event.element.statement : event.statement;
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			if (currentToken.attr(AttributeEnum.TYPE) != null) {
				return null;
			}
			if (currentToken.isVariable()) {// variable
				String variableName = currentToken.toString();
				IType type = findType(clazz, context, variableName);
				Assert.notNull(type, "Variable must be declared!variableName:" + variableName);
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isArrayIndex()) {// .strs[0]
				String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
				IType type = findType(clazz, context, memberName);
				Assert.notNull(type, "Variable must be declared!variableName:" + memberName);
				// 转换数组类型为目标类型
				type = type.getTargetType();
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isKeyword() && KeywordEnum.isKeywordVariable(currentToken.getValue())) {
				String variableName = currentToken.toString();
				IType type = findType(clazz, context, variableName);
				Assert.notNull(type, "Variable must be declared!variableName:" + variableName);
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

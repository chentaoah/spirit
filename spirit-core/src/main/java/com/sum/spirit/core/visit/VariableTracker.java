package com.sum.spirit.core.visit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.api.StatementAction;
import com.sum.spirit.core.ClassVisiter;
import com.sum.spirit.core.link.TypeFactory;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.ElementEvent;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.common.StatementEvent;
import com.sum.spirit.pojo.element.impl.Statement;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
@Order(-60)
public class VariableTracker extends AbsElementAction implements StatementAction {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;

	@Override
	public void visit(ElementEvent event) {
		IClass clazz = event.clazz;
		MethodContext context = event.context;
		Statement statement = event.element.statement;
		doVisit(clazz, context, statement);
	}

	@Override
	public void visit(StatementEvent event) {
		IClass clazz = event.clazz;
		MethodContext context = event.context;
		Statement statement = event.statement;
		doVisit(clazz, context, statement);
	}

	public void doVisit(IClass clazz, MethodContext context, Statement statement) {
		new StmtVisiter().visit(statement, (stmt, index, currentToken) -> {
			if (currentToken.attr(AttributeEnum.TYPE) != null) {
				return null;
			}
			if (currentToken.isVariable()) {// variable
				String variableName = currentToken.toString();
				IType type = getVariableType(clazz, context, variableName);
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isArrayIndex()) {// .strs[0]
				String memberName = currentToken.attr(AttributeEnum.MEMBER_NAME);
				IType type = getVariableType(clazz, context, memberName);
				type = type.getTargetType();// 转换数组类型为目标类型
				currentToken.setAttr(AttributeEnum.TYPE, type);

			} else if (currentToken.isKeyword() && KeywordEnum.isKeywordVariable(currentToken.getValue())) {
				String variableName = currentToken.toString();
				IType type = findKeywordType(clazz, variableName);
				currentToken.setAttr(AttributeEnum.TYPE, type);
			}
			return null;
		});
	}

	public IType findKeywordType(IClass clazz, String variableName) {
		if (KeywordEnum.isSuper(variableName)) {// super
			return clazz.getSuperType().toSuper();

		} else if (KeywordEnum.isThis(variableName)) {// this
			return clazz.getType().toThis();
		}
		throw new RuntimeException("Variable must be declared!variableName:" + variableName);
	}

	public IType findTypeByContext(MethodContext context, String variableName) {
		if (context != null) {
			return context.findVariableType(variableName);
		}
		return null;
	}

	public IType findTypeByInherit(IClass clazz, String variableName) {
		try {
			// 从本身和父类里面寻找，父类可能是native的
			return linker.visitField(clazz.getType().toThis(), variableName);

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

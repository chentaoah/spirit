package com.sum.spirit.core.visiter.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.entity.Statement;
import com.sum.spirit.common.entity.Token;
import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.api.StatementAction;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.visiter.ClassVisiter;
import com.sum.spirit.core.visiter.entity.ElementEvent;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.entity.MethodContext;
import com.sum.spirit.core.visiter.entity.StatementEvent;
import com.sum.spirit.core.visiter.linker.TypeFactory;
import com.sum.spirit.core.visiter.utils.StmtVisiter;

import cn.hutool.core.lang.Assert;

@Component
@Order(-60)
public class VariableTracker extends AbstractElementAction implements StatementAction {

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
		new StmtVisiter().visitVoid(statement, event -> {
			Token token = event.item;
			if (token.attr(AttributeEnum.TYPE) != null) {
				return;
			}
			if (token.isVariable()) {// variable
				String variableName = token.toString();
				IType type = getVariableType(clazz, context, variableName);
				token.setAttr(AttributeEnum.TYPE, type);

			} else if (token.isArrayIndex()) {// .strs[0]
				String memberName = token.attr(AttributeEnum.MEMBER_NAME);
				IType type = getVariableType(clazz, context, memberName);
				type = type.getTargetType();// 转换数组类型为目标类型
				token.setAttr(AttributeEnum.TYPE, type);

			} else if (token.isKeyword() && KeywordEnum.isKeywordVariable(token.getValue())) {
				String variableName = token.toString();
				IType type = findKeywordType(clazz, variableName);
				token.setAttr(AttributeEnum.TYPE, type);
			}
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

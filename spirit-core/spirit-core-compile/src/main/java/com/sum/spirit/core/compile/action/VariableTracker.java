package com.sum.spirit.core.compile.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.ClassVisiter;
import com.sum.spirit.core.compile.deduce.TypeDerivator;
import com.sum.spirit.core.compile.entity.ElementEvent;
import com.sum.spirit.core.compile.entity.MethodContext;
import com.sum.spirit.core.compile.linker.TypeFactory;
import com.sum.spirit.core.compile.utils.StmtVisiter;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
@Order(-60)
public class VariableTracker extends AbstractElementAction {

	@Autowired
	public ClassVisiter visiter;
	@Autowired
	public ClassLinker linker;
	@Autowired
	public TypeFactory factory;
	@Autowired
	public TypeDerivator derivator;

	@Override
	public void visit(ElementEvent event) {
		doVisit(event.clazz, event.context, event.element);
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
				type = derivator.getTargetType(type);// 转换数组类型为目标类型
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
			return derivator.toSuper(derivator.getSuperType(clazz));

		} else if (KeywordEnum.isThis(variableName)) {// this
			return derivator.toThis(clazz.getType());
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
			return linker.visitField(derivator.toThis(clazz.getType()), variableName);

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

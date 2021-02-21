package com.sum.spirit.java.visiter;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.visiter.entity.StaticTypes;
import com.sum.spirit.java.utils.TypeUtils;

@Component
@Order(-60)
public class StringLogicAction extends AbstractTreeElementAction {

	public static final String FORMAT = "StringUtils.isNotEmpty(%s)";

	@Override
	public boolean isTrigger(Token currentToken) {
		return currentToken.isLogical() && (currentToken.isNegate() || currentToken.isLogicAnd() || currentToken.isLogicOr());
	}

	@Override
	public void visit(IClass clazz, Statement statement, int index, Token currentToken) {
		Map<String, Object> context = new HashMap<>();
		if (currentToken.isNegate()) {
			visitNext(clazz, statement, index, currentToken, context);

		} else if (currentToken.isLogicAnd() || currentToken.isLogicOr()) {
			visitPrev(clazz, statement, index, currentToken, context);
			visitNext(clazz, statement, index, currentToken, context);
		}
	}

	@Override
	public void doVisitPrev(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		int start = (Integer) context.get(START);
		Statement prevStatement = (Statement) context.get(PREV_STATEMENT);
		IType prevType = (IType) context.get(PREV_TYPE);
		if (TypeUtils.isString(prevType)) {
			String text = String.format(FORMAT, prevStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, StaticTypes.BOOLEAN);
			expressToken.setAttr(AttributeEnum.TREE_ID, currentToken.attr(AttributeEnum.TREE_ID) + "-0");
			statement.replaceTokens(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

	@Override
	public void doVisitNext(IClass clazz, Statement statement, int index, Token currentToken, Map<String, Object> context) {
		int end = (Integer) context.get(END);
		Statement nextStatement = (Statement) context.get(NEXT_STATEMENT);
		IType nextType = (IType) context.get(NEXT_TYPE);
		if (TypeUtils.isString(nextType)) {
			String text = String.format(FORMAT, nextStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, StaticTypes.BOOLEAN);
			expressToken.setAttr(AttributeEnum.TREE_ID, currentToken.attr(AttributeEnum.TREE_ID) + "-1");
			statement.replaceTokens(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

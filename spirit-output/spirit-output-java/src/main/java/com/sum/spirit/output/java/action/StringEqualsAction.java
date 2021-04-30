package com.sum.spirit.output.java.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.annotation.Native;
import com.sum.spirit.common.constants.Attribute;
import com.sum.spirit.common.enums.TokenTypeEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.StaticTypes;
import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.output.java.utils.TypeUtils;

@Native
@Component
@Order(-60)
public class StringEqualsAction extends AbstractTreeElementAction {

	public static final String FORMAT = "StringUtils.equals(%s, %s)";

	@Override
	public boolean isTrigger(Token currentToken) {
		return currentToken.isEquals() || currentToken.isUnequals();
	}

	@Override
	public void visit(IClass clazz, Statement statement, int index, Token currentToken) {
		Map<String, Object> context = new HashMap<>();
		visitPrev(clazz, statement, index, currentToken, context);
		int start = (Integer) context.get(START);
		Statement prevStatement = (Statement) context.get(PREV_STATEMENT);
		IType prevType = (IType) context.get(PREV_TYPE);
		if (!TypeUtils.isString(prevType)) {
			return;
		}
		visitNext(clazz, statement, index, currentToken, context);
		int end = (Integer) context.get(END);
		Statement nextStatement = (Statement) context.get(NEXT_STATEMENT);
		IType nextType = (IType) context.get(NEXT_TYPE);
		if (TypeUtils.isString(nextType)) {
			String format = currentToken.isEquals() ? FORMAT : "!" + FORMAT;
			String text = String.format(format, prevStatement, nextStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(Attribute.TYPE, StaticTypes.BOOLEAN);
			expressToken.setAttr(Attribute.TREE_ID, currentToken.attr(Attribute.TREE_ID));
			statement.replaceTokens(start, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

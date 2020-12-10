package com.sum.spirit.java.visit;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
@Order(-80)
public class StrEqualsAction extends AbsTreeElementAction {

	public static final String FORMAT = "StringUtils.equals(%s, %s)";

	@Override
	public boolean isTrigger(Token currentToken) {
		return currentToken.isEquals() || currentToken.isUnequals();
	}

	@Override
	public void visit(IClass clazz, Statement statement, int index, Token currentToken) {
		Map<String, Object> context = new HashMap<>();
		visitPrev(clazz, statement, index, currentToken, context);
		int start = (Integer) context.get("start");
		Statement prevStatement = (Statement) context.get("prevStatement");
		IType prevType = (IType) context.get("prevType");
		if (!TypeUtils.isString(prevType)) {
			return;
		}
		visitNext(clazz, statement, index, currentToken, context);
		int end = (Integer) context.get("end");
		Statement nextStatement = (Statement) context.get("nextStatement");
		IType nextType = (IType) context.get("nextType");
		if (TypeUtils.isString(nextType)) {
			String format = currentToken.isEquals() ? FORMAT : "!" + FORMAT;
			String text = String.format(format, prevStatement, nextStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
			expressToken.setAttr(AttributeEnum.TREE_ID, currentToken.attr(AttributeEnum.TREE_ID));
			statement.replaceTokens(start, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

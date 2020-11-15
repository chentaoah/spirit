package com.sum.spirit.java.core.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.type.IType;
import com.sum.spirit.core.visit.FastDeducer;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.java.utils.TreeUtils;
import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.AttributeEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

@Component
@Order(-60)
public class StrLogicalConverter implements ElementConverter {

	public static final String FORMAT = "StringUtils.isNotEmpty(%s)";

	@Autowired
	public FastDeducer deducer;

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.statement);
	}

	public void convertStmt(IClass clazz, Statement statement) {
		// 先处理子节点，下层节点的结果，会间接影响上层
		for (Token token : statement.tokens) {
			if (token.canSplit())
				convertStmt(clazz, token.getValue());
		}
		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.isLogical()) {
				if (token.isNegate()) {
					replaceFollowingStr(clazz, statement, index, token);

				} else if (token.isLogicAnd() || token.isLogicOr()) {
					replacePreviousStr(clazz, statement, index, token);
					replaceFollowingStr(clazz, statement, index, token);
				}
			}
		}
	}

	public void replacePreviousStr(IClass clazz, Statement statement, int index, Token token) {
		int start = TreeUtils.findStart(statement, index);
		Statement lastStatement = statement.subStmt(start, index);
		IType type = deducer.derive(clazz, lastStatement);
		if (TypeUtils.isStr(type)) {
			String text = String.format(FORMAT, lastStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
			expressToken.setAttr(AttributeEnum.TREE_ID, token.attr(AttributeEnum.TREE_ID) + "-0");
			statement.replaceTokens(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

	public void replaceFollowingStr(IClass clazz, Statement statement, int index, Token token) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType type = deducer.derive(clazz, nextStatement);
		if (TypeUtils.isStr(type)) {
			String text = String.format(FORMAT, nextStatement);
			Token expressToken = new Token(TokenTypeEnum.CUSTOM_EXPRESS, text);
			expressToken.setAttr(AttributeEnum.TYPE, TypeEnum.boolean_t.value);
			expressToken.setAttr(AttributeEnum.TREE_ID, token.attr(AttributeEnum.TREE_ID) + "-1");
			statement.replaceTokens(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

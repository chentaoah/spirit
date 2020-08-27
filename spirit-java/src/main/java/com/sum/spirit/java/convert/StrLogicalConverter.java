package com.sum.spirit.java.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.java.utils.TypeUtils;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.TreeUtils;

@Component
@Order(-60)
public class StrLogicalConverter implements ElementConverter {

	@Autowired
	public FastDeducer deducer;

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.statement);
	}

	public void convertStmt(IClass clazz, Statement statement) {
		// Process the child nodes first, or it will affect the transformation of the
		// upper layer
		for (Token token : statement.tokens) {
			if (token.canSplit())
				convertStmt(clazz, token.getValue());
		}

		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.isOperator() && ("!".equals(token.toString()) || "&&".equals(token.toString()) || "||".equals(token.toString()))) {

				if ("!".equals(token.toString())) {
					replaceFollowingStr(clazz, statement, index, token);

				} else if ("&&".equals(token.toString()) || "||".equals(token.toString())) {
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
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, lastStatement);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
			expressToken.getTreeId().set(token.getTreeId().get() + "-0");
			statement.replace(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

	public void replaceFollowingStr(IClass clazz, Statement statement, int index, Token token) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextStatement = statement.subStmt(index + 1, end);
		IType type = deducer.derive(clazz, nextStatement);
		if (TypeUtils.isStr(type)) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, nextStatement);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
			expressToken.getTreeId().set(token.getTreeId().get() + "-1");
			statement.replace(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

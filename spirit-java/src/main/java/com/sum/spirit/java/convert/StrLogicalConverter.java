package com.sum.spirit.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.convert.ElementConverter;
import com.sum.spirit.api.deduce.FastDeducer;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.TypeTable;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.utils.TreeUtils;

@Order(-60)
public class StrLogicalConverter implements ElementConverter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

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
		Statement lastSubStmt = statement.subStmt(start, index);
		IType type = deducer.derive(clazz, lastSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, lastSubStmt);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
			expressToken.getTreeId().set(token.getTreeId().get() + "-0");
			statement.replace(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

	public void replaceFollowingStr(IClass clazz, Statement statement, int index, Token token) {
		int end = TreeUtils.findEnd(statement, index);
		Statement nextSubStmt = statement.subStmt(index + 1, end);
		IType type = deducer.derive(clazz, nextSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, nextSubStmt);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(TypeTable.BOOLEAN_TYPE);
			expressToken.getTreeId().set(token.getTreeId().get() + "-1");
			statement.replace(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}
	}

}

package com.sum.shy.java.convert;

import com.sum.pisces.api.annotation.Order;
import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.convert.ElementConverter;
import com.sum.shy.api.deduce.FastDeducer;
import com.sum.shy.lib.StringUtils;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.pojo.element.Statement;
import com.sum.shy.pojo.element.Token;
import com.sum.shy.utils.TreeUtils;

@Order(-60)
public class StrLogicalConverter implements ElementConverter {

	public static FastDeducer deducer = ProxyFactory.get(FastDeducer.class);

	@Override
	public void convert(IClass clazz, Element element) {
		convertStmt(clazz, element.stmt);
	}

	public void convertStmt(IClass clazz, Statement stmt) {
		// 如果有子节点，先处理子节点
		for (Token token : stmt.tokens) {
			if (token.canSplit())
				convertStmt(clazz, token.getValue());
		}

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isOperator() && ("!".equals(token.toString()) || "&&".equals(token.toString()) || "||".equals(token.toString()))) {

				if ("!".equals(token.toString())) {
					replaceFollowingStr(clazz, stmt, i, token);

				} else if ("&&".equals(token.toString()) || "||".equals(token.toString())) {
					replacePreviousStr(clazz, stmt, i, token);
					replaceFollowingStr(clazz, stmt, i, token);

				}
			}
		}
	}

	public void replacePreviousStr(IClass clazz, Statement stmt, int index, Token token) {
		int start = TreeUtils.findStart(stmt, index);
		Statement lastSubStmt = stmt.subStmt(start, index);
		IType type = deducer.derive(clazz, lastSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, lastSubStmt);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(StaticType.BOOLEAN_TYPE);
			expressToken.setTreeId(token.getTreeId() + "-0");
			stmt.replace(start, index, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}

	}

	public void replaceFollowingStr(IClass clazz, Statement stmt, int index, Token token) {
		int end = TreeUtils.findEnd(stmt, index);
		Statement nextSubStmt = stmt.subStmt(index + 1, end);
		IType type = deducer.derive(clazz, nextSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, nextSubStmt);
			Token expressToken = new Token(Constants.CUSTOM_EXPRESS_TOKEN, text);
			expressToken.setTypeAtt(StaticType.BOOLEAN_TYPE);
			expressToken.setTreeId(token.getTreeId() + "-1");
			stmt.replace(index + 1, end, expressToken);
			clazz.addImport(StringUtils.class.getName());
		}

	}

}

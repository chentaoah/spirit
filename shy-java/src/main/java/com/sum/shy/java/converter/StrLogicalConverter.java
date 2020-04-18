package com.sum.shy.java.converter;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IType;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.metadata.StaticType;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.utils.TreeUtils;
import com.sum.shy.lib.StringUtils;

public class StrLogicalConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {
		// 如果有子节点，先处理子节点
		for (Token token : stmt.tokens) {
			if (token.canVisit())
				convertStmt(clazz, token.getStmt());
		}

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isOperator() && ("!".equals(token.toString()) || "&&".equals(token.toString())
					|| "||".equals(token.toString()))) {

				if ("!".equals(token.toString())) {
					replaceFollowingStr(clazz, stmt, i, token);

				} else if ("&&".equals(token.toString()) || "||".equals(token.toString())) {
					replacePreviousStr(clazz, stmt, i, token);
					replaceFollowingStr(clazz, stmt, i, token);

				}
			}
		}
	}

	public static void replacePreviousStr(IClass clazz, Stmt stmt, int index, Token token) {
		int start = TreeUtils.findStart(stmt, index);
		Stmt lastSubStmt = stmt.subStmt(start, index);
		IType type = FastDeducer.deriveStmt(clazz, lastSubStmt);
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

	public static void replaceFollowingStr(IClass clazz, Stmt stmt, int index, Token token) {
		int end = TreeUtils.findEnd(stmt, index);
		Stmt nextSubStmt = stmt.subStmt(index + 1, end);
		IType type = FastDeducer.deriveStmt(clazz, nextSubStmt);
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

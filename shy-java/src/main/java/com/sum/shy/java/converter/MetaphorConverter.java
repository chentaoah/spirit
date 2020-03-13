package com.sum.shy.java.converter;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.type.api.IType;
import com.sum.shy.lib.StringUtils;

public class MetaphorConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {
		// 如果有子节点，先处理子节点
		for (Token token : stmt.tokens) {
			if (token.hasStmt())
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

	private static void replacePreviousStr(IClass clazz, Stmt stmt, int index, Token token) {
		int start = 0;
		for (int j = index - 1; j >= 0; j--) {
			Token lastToken = stmt.getToken(j);
			if (lastToken.getTreeId() != null && lastToken.getTreeId().startsWith(token.getTreeId())) {
				start = j;
			} else {
				break;
			}
		}
		Stmt lastSubStmt = stmt.subStmt(start, index);
		IType type = FastDeducer.deriveStmt(clazz, lastSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, lastSubStmt);
			stmt.replace(start, index, new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));
			clazz.addImport(StringUtils.class.getName());
		}

	}

	private static void replaceFollowingStr(IClass clazz, Stmt stmt, int index, Token token) {
		int end = stmt.size();
		for (int j = index + 1; j < stmt.size(); j++) {
			Token nextToken = stmt.getToken(j);
			if (nextToken.getTreeId() != null && nextToken.getTreeId().startsWith(token.getTreeId())) {
				end = j + 1;
			} else {
				break;
			}
		}
		Stmt nextSubStmt = stmt.subStmt(index + 1, end);
		IType type = FastDeducer.deriveStmt(clazz, nextSubStmt);
		if (type.isStr()) {
			String format = "StringUtils.isNotEmpty(%s)";
			String text = String.format(format, nextSubStmt);
			stmt.replace(index + 1, end, new Token(Constants.CUSTOM_EXPRESS_TOKEN, text));
			clazz.addImport(StringUtils.class.getName());
		}

	}

}

package com.sum.shy.java.converter;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.processor.FastDeducer;
import com.sum.shy.core.type.api.IType;

public class SymbolConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isOperator() && ("==".equals(token.toString()) || "!=".equals(token.toString()))) {
				// 向左遍历获取自己的分支
				int start = 0;
				for (int j = i - 1; j >= 0; j--) {
					Token lastToken = stmt.getToken(j);
					if (lastToken.getTreeId().startsWith(token.getTreeId())) {
						start = j;
					} else {
						break;
					}
				}
				int end = stmt.size();
				for (int j = i + 1; j < stmt.size(); j++) {
					Token nextToken = stmt.getToken(j);
					if (nextToken.getTreeId().startsWith(token.getTreeId())) {
						end = j;
					} else {
						break;
					}
				}
				// 截取出这一部分
				Stmt lastSubStmt = stmt.subStmt(start, i);
				IType type = FastDeducer.deriveStmt(clazz, lastSubStmt);
				if (type.isStr()) {
					Stmt nextSubStmt = stmt.subStmt(i, end);
					stmt.replace(start, end, new Token(Constants.CUSTOM_EXPRESS_TOKEN, ""));
				}
			}
		}
	}

}

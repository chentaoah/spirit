package com.sum.shy.java.converter;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;

public class SymbolConverter {

	public static void convertStmt(IClass clazz, Stmt stmt) {

		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isOperator() && ("==".equals(token.toString()) || "!=".equals(token.toString()))) {
				// 向左遍历，并截取出，优先级比自身高的那部分

			}
		}
	}

}

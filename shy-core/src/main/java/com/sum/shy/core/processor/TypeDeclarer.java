package com.sum.shy.core.processor;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.document.Stmt;
import com.sum.shy.core.document.Token;
import com.sum.shy.core.type.CodeType;

public class TypeDeclarer {

	public static void declareStmt(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.hasSubStmt()) {
				declareStmt(clazz, token.getStmt());

			} else if (token.isType()) {
				if (i + 1 < stmt.size()) {
					Token nextToken = stmt.getToken(i + 1);
					if (nextToken.isVar()) {
						nextToken.setTypeAtt(new CodeType(clazz, token));
					}
				}
			}
		}
	}

}
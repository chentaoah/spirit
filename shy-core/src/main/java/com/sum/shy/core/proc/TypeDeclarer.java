package com.sum.shy.core.proc;

import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.doc.Token;
import com.sum.shy.core.type.CodeType;

public class TypeDeclarer {

	public static void declareStmt(IClass clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.hasSubStmt()) {
				declareStmt(clazz, token.getSubStmt());

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

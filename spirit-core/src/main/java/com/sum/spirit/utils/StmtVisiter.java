package com.sum.spirit.utils;

import com.sum.spirit.pojo.element.Statement;
import com.sum.spirit.pojo.element.Token;

public class StmtVisiter {

	public void visit(Statement statement, SimpleAction action) {
		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.canSplit()) {
				Statement subStatement = token.getValue();
				visit(subStatement, action);
			}
			if (action instanceof SimpleAction) {
				Token returnToken = ((SimpleAction) action).execute(statement, index, token);
				if (returnToken != null) {
					statement.setToken(index, returnToken);
				}
			}
		}
	}

	public static interface Action {
	}

	public static interface SimpleAction extends Action {
		Token execute(Statement statement, int index, Token currentToken);
	}

}

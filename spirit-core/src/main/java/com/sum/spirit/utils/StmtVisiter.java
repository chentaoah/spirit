package com.sum.spirit.utils;

import com.sum.spirit.pojo.element.impl.Statement;
import com.sum.spirit.pojo.element.impl.Token;

public class StmtVisiter {

	public void visit(Statement statement, SimpleAction action) {
		for (int index = 0; index < statement.size(); index++) {
			Token token = statement.getToken(index);
			if (token.canSplit()) {
				Statement subStatement = token.getValue();
				visit(subStatement, action);
			}
			Token returnToken = action.execute(statement, index, token);
			if (returnToken != null) {
				statement.setToken(index, returnToken);
			}
		}
	}

	public static interface SimpleAction {
		Token execute(Statement statement, int index, Token currentToken);
	}

}

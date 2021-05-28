package com.gitee.spirit.core.element.utils;

import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

public class StmtVisiter {

	public static void visit(Statement statement, Consumer<Statement> consumer) {
		for (Token token : statement) {
			if (token.hasSubStmt()) {
				visit(token.getValue(), consumer);
			}
		}
		consumer.accept(statement);
	}

	public static interface Consumer<T> {
		void accept(T t);
	}

}

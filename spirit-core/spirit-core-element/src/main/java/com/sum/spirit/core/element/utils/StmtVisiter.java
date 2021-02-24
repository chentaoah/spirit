package com.sum.spirit.core.element.utils;

import com.sum.spirit.core.element.entity.Statement;
import com.sum.spirit.core.element.entity.Token;

public class StmtVisiter {

	public static void visit(Statement statement, Consumer<Statement> consumer) {
		for (Token token : statement) {
			if (token.canSplit()) {
				visit(token.getValue(), consumer);
			}
		}
		consumer.accept(statement);
	}

	public static interface Consumer<T> {
		void accept(T t);
	}

}

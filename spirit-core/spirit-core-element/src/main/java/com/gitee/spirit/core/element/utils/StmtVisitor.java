package com.gitee.spirit.core.element.utils;

import java.util.function.Consumer;

import com.gitee.spirit.core.element.entity.Statement;
import com.gitee.spirit.core.element.entity.Token;

public class StmtVisitor {

    public static void forEachStmt(Statement statement, Consumer<Statement> consumer) {
        for (Token token : statement) {
            if (token.hasSubStmt()) {
                forEachStmt(token.getValue(), consumer);
            }
        }
        consumer.accept(statement);
    }

    public static void forEachToken(Statement statement, Consumer<Token> consumer) {
        for (Token token : statement) {
            if (token.hasSubStmt()) {
                forEachToken(token.getValue(), consumer);
            }
            consumer.accept(token);
        }
    }

}

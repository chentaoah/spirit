package com.sum.spirit.core.element.frame;

import java.util.List;

import com.sum.spirit.common.enums.SyntaxEnum;
import com.sum.spirit.core.element.entity.Token;

public abstract class Syntactic extends TokenBox {

	public SyntaxEnum syntax;

	public Syntactic(SyntaxEnum syntax, List<Token> tokens) {
		super(tokens);
		this.syntax = syntax;
	}

	public boolean isImport() {
		return syntax == SyntaxEnum.IMPORT;
	}

	public boolean isAnnotation() {
		return syntax == SyntaxEnum.ANNOTATION;
	}

	public boolean isInterface() {
		return syntax == SyntaxEnum.INTERFACE;
	}

	public boolean isAbstract() {
		return syntax == SyntaxEnum.ABSTRACT;
	}

	public boolean isClass() {
		return syntax == SyntaxEnum.CLASS;
	}

	public boolean isFieldAssign() {
		return syntax == SyntaxEnum.FIELD_ASSIGN;
	}

	public boolean isFuncDeclare() {
		return syntax == SyntaxEnum.FUNC_DECLARE;
	}

	public boolean isFunc() {
		return syntax == SyntaxEnum.FUNC;
	}

	public boolean isSuper() {
		return syntax == SyntaxEnum.SUPER;
	}

	public boolean isThis() {
		return syntax == SyntaxEnum.THIS;
	}

	public boolean isDeclare() {
		return syntax == SyntaxEnum.DECLARE;
	}

	public boolean isDeclareAssign() {
		return syntax == SyntaxEnum.DECLARE_ASSIGN;
	}

	public boolean isAssign() {
		return syntax == SyntaxEnum.ASSIGN;
	}

	public boolean isInvoke() {
		return syntax == SyntaxEnum.INVOKE;
	}

	public boolean isReturn() {
		return syntax == SyntaxEnum.RETURN;
	}

	public boolean isIf() {
		return syntax == SyntaxEnum.IF;
	}

	public boolean isElseIf() {
		return syntax == SyntaxEnum.ELSE_IF;
	}

	public boolean isElse() {
		return syntax == SyntaxEnum.ELSE;
	}

	public boolean isEnd() {
		return syntax == SyntaxEnum.END;
	}

	public boolean isFor() {
		return syntax == SyntaxEnum.FOR;
	}

	public boolean isForIn() {
		return syntax == SyntaxEnum.FOR_IN;
	}

	public boolean isWhile() {
		return syntax == SyntaxEnum.WHILE;
	}

	public boolean isContinue() {
		return syntax == SyntaxEnum.CONTINUE;
	}

	public boolean isBreak() {
		return syntax == SyntaxEnum.BREAK;
	}

	public boolean isTry() {
		return syntax == SyntaxEnum.TRY;
	}

	public boolean isCatch() {
		return syntax == SyntaxEnum.CATCH;
	}

	public boolean isFinally() {
		return syntax == SyntaxEnum.FINALLY;
	}

	public boolean isThrow() {
		return syntax == SyntaxEnum.THROW;
	}

	public boolean isSync() {
		return syntax == SyntaxEnum.SYNC;
	}

	public boolean isPrint() {
		return syntax == SyntaxEnum.PRINT;
	}

	public boolean isDebug() {
		return syntax == SyntaxEnum.DEBUG;
	}

	public boolean isError() {
		return syntax == SyntaxEnum.ERROR;
	}

}

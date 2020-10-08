package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.SyntaxEnum;

public abstract class Syntactic extends TokenBox {

	public boolean isImport() {
		return getSyntax() == SyntaxEnum.IMPORT;
	}

	public boolean isAnnotation() {
		return getSyntax() == SyntaxEnum.ANNOTATION;
	}

	public boolean isInterface() {
		return getSyntax() == SyntaxEnum.INTERFACE;
	}

	public boolean isAbstract() {
		return getSyntax() == SyntaxEnum.ABSTRACT;
	}

	public boolean isClass() {
		return getSyntax() == SyntaxEnum.CLASS;
	}

	public boolean isFieldAssign() {
		return getSyntax() == SyntaxEnum.FIELD_ASSIGN;
	}

	public boolean isFuncDeclare() {
		return getSyntax() == SyntaxEnum.FUNC_DECLARE;
	}

	public boolean isFunc() {
		return getSyntax() == SyntaxEnum.FUNC;
	}

	public boolean isSuper() {
		return getSyntax() == SyntaxEnum.SUPER;
	}

	public boolean isThis() {
		return getSyntax() == SyntaxEnum.THIS;
	}

	public boolean isDeclare() {
		return getSyntax() == SyntaxEnum.DECLARE;
	}

	public boolean isDeclareAssign() {
		return getSyntax() == SyntaxEnum.DECLARE_ASSIGN;
	}

	public boolean isAssign() {
		return getSyntax() == SyntaxEnum.ASSIGN;
	}

	public boolean isInvoke() {
		return getSyntax() == SyntaxEnum.INVOKE;
	}

	public boolean isReturn() {
		return getSyntax() == SyntaxEnum.RETURN;
	}

	public boolean isIf() {
		return getSyntax() == SyntaxEnum.IF;
	}

	public boolean isElseIf() {
		return getSyntax() == SyntaxEnum.ELSE_IF;
	}

	public boolean isElse() {
		return getSyntax() == SyntaxEnum.ELSE;
	}

	public boolean isEnd() {
		return getSyntax() == SyntaxEnum.END;
	}

	public boolean isFor() {
		return getSyntax() == SyntaxEnum.FOR;
	}

	public boolean isForIn() {
		return getSyntax() == SyntaxEnum.FOR_IN;
	}

	public boolean isWhile() {
		return getSyntax() == SyntaxEnum.WHILE;
	}

	public boolean isContinue() {
		return getSyntax() == SyntaxEnum.CONTINUE;
	}

	public boolean isBreak() {
		return getSyntax() == SyntaxEnum.BREAK;
	}

	public boolean isTry() {
		return getSyntax() == SyntaxEnum.TRY;
	}

	public boolean isCatch() {
		return getSyntax() == SyntaxEnum.CATCH;
	}

	public boolean isFinally() {
		return getSyntax() == SyntaxEnum.FINALLY;
	}

	public boolean isThrow() {
		return getSyntax() == SyntaxEnum.THROW;
	}

	public boolean isSync() {
		return getSyntax() == SyntaxEnum.SYNC;
	}

	public boolean isPrint() {
		return getSyntax() == SyntaxEnum.PRINT;
	}

	public boolean isDebug() {
		return getSyntax() == SyntaxEnum.DEBUG;
	}

	public boolean isError() {
		return getSyntax() == SyntaxEnum.ERROR;
	}

	public abstract SyntaxEnum getSyntax();

}

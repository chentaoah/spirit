package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.common.SyntaxEnum;

public abstract class Syntactic extends TokenBox {

	public boolean isImport() {
		return SyntaxEnum.IMPORT.equals(getSyntax());
	}

	public boolean isAnnotation() {
		return SyntaxEnum.ANNOTATION.equals(getSyntax());
	}

	public boolean isInterface() {
		return SyntaxEnum.INTERFACE.equals(getSyntax());
	}

	public boolean isAbstract() {
		return SyntaxEnum.ABSTRACT.equals(getSyntax());
	}

	public boolean isClass() {
		return SyntaxEnum.CLASS.equals(getSyntax());
	}

	public boolean isFieldAssign() {
		return SyntaxEnum.FIELD_ASSIGN.equals(getSyntax());
	}

	public boolean isFuncDeclare() {
		return SyntaxEnum.FUNC_DECLARE.equals(getSyntax());
	}

	public boolean isFunc() {
		return SyntaxEnum.FUNC.equals(getSyntax());
	}

	public boolean isSuper() {
		return SyntaxEnum.SUPER.equals(getSyntax());
	}

	public boolean isThis() {
		return SyntaxEnum.THIS.equals(getSyntax());
	}

	public boolean isDeclare() {
		return SyntaxEnum.DECLARE.equals(getSyntax());
	}

	public boolean isDeclareAssign() {
		return SyntaxEnum.DECLARE_ASSIGN.equals(getSyntax());
	}

	public boolean isAssign() {
		return SyntaxEnum.ASSIGN.equals(getSyntax());
	}

	public boolean isInvoke() {
		return SyntaxEnum.INVOKE.equals(getSyntax());
	}

	public boolean isReturn() {
		return SyntaxEnum.RETURN.equals(getSyntax());
	}

	public boolean isIf() {
		return SyntaxEnum.IF.equals(getSyntax());
	}

	public boolean isElseIf() {
		return SyntaxEnum.ELSE_IF.equals(getSyntax());
	}

	public boolean isElse() {
		return SyntaxEnum.ELSE.equals(getSyntax());
	}

	public boolean isEnd() {
		return SyntaxEnum.END.equals(getSyntax());
	}

	public boolean isFor() {
		return SyntaxEnum.FOR.equals(getSyntax());
	}

	public boolean isForIn() {
		return SyntaxEnum.FOR_IN.equals(getSyntax());
	}

	public boolean isWhile() {
		return SyntaxEnum.WHILE.equals(getSyntax());
	}

	public boolean isContinue() {
		return SyntaxEnum.CONTINUE.equals(getSyntax());
	}

	public boolean isBreak() {
		return SyntaxEnum.BREAK.equals(getSyntax());
	}

	public boolean isTry() {
		return SyntaxEnum.TRY.equals(getSyntax());
	}

	public boolean isCatch() {
		return SyntaxEnum.CATCH.equals(getSyntax());
	}

	public boolean isFinally() {
		return SyntaxEnum.FINALLY.equals(getSyntax());
	}

	public boolean isThrow() {
		return SyntaxEnum.THROW.equals(getSyntax());
	}

	public boolean isSync() {
		return SyntaxEnum.SYNC.equals(getSyntax());
	}

	public boolean isPrint() {
		return SyntaxEnum.PRINT.equals(getSyntax());
	}

	public boolean isDebug() {
		return SyntaxEnum.DEBUG.equals(getSyntax());
	}

	public boolean isError() {
		return SyntaxEnum.ERROR.equals(getSyntax());
	}

	public abstract SyntaxEnum getSyntax();

}

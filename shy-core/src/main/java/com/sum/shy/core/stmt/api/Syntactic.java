package com.sum.shy.core.stmt.api;

import com.sum.shy.core.entity.Constants;

/**
 * 可得句法的
 * 
 * @author chentao26275
 *
 */
public abstract class Syntactic extends TokenBox {

	public boolean isImport() {
		return Constants.IMPORT_SYNTAX.equals(getSyntax());
	}

	public boolean isAnnotation() {
		return Constants.ANNOTATION_SYNTAX.equals(getSyntax());
	}

	public boolean isInterface() {
		return Constants.INTERFACE_SYNTAX.equals(getSyntax());
	}

	public boolean isAbstract() {
		return Constants.ABSTRACT_SYNTAX.equals(getSyntax());
	}

	public boolean isClass() {
		return Constants.CLASS_SYNTAX.equals(getSyntax());
	}

	public boolean isDeclare() {
		return Constants.DECLARE_SYNTAX.equals(getSyntax());
	}

	public boolean isDeclareAssign() {
		return Constants.DECLARE_ASSIGN_SYNTAX.equals(getSyntax());
	}

	public boolean isAssign() {
		return Constants.ASSIGN_SYNTAX.equals(getSyntax());
	}

	public boolean isFuncDeclare() {
		return Constants.FUNC_DECLARE_SYNTAX.equals(getSyntax());
	}

	public boolean isFunc() {
		return Constants.FUNC_SYNTAX.equals(getSyntax());
	}

	public boolean isSuper() {
		return Constants.SUPER_SYNTAX.equals(getSyntax());
	}

	public boolean isThis() {
		return Constants.THIS_SYNTAX.equals(getSyntax());
	}

	public boolean isFieldAssign() {
		return Constants.FIELD_ASSIGN_SYNTAX.equals(getSyntax());
	}

	public boolean isInvoke() {
		return Constants.INVOKE_SYNTAX.equals(getSyntax());
	}

	public boolean isReturn() {
		return Constants.RETURN_SYNTAX.equals(getSyntax());
	}

	public boolean isIf() {
		return Constants.IF_SYNTAX.equals(getSyntax());
	}

	public boolean isElseIf() {
		return Constants.ELSEIF_SYNTAX.equals(getSyntax());
	}

	public boolean isElse() {
		return Constants.ELSE_SYNTAX.equals(getSyntax());
	}

	public boolean isEnd() {
		return Constants.END_SYNTAX.equals(getSyntax());
	}

	public boolean isFor() {
		return Constants.FOR_SYNTAX.equals(getSyntax());
	}

	public boolean isForIn() {
		return Constants.FOR_IN_SYNTAX.equals(getSyntax());
	}

	public boolean isWhile() {
		return Constants.WHILE_SYNTAX.equals(getSyntax());
	}

	public boolean isContinue() {
		return Constants.CONTINUE_SYNTAX.equals(getSyntax());
	}

	public boolean isBreak() {
		return Constants.BREAK_SYNTAX.equals(getSyntax());
	}

	public boolean isTry() {
		return Constants.TRY_SYNTAX.equals(getSyntax());
	}

	public boolean isCatch() {
		return Constants.CATCH_SYNTAX.equals(getSyntax());
	}

	public boolean isFinally() {
		return Constants.FINALLY_SYNTAX.equals(getSyntax());
	}

	public boolean isThrow() {
		return Constants.THROW_SYNTAX.equals(getSyntax());
	}

	public boolean isSync() {
		return Constants.SYNC_SYNTAX.equals(getSyntax());
	}

	public boolean isPrint() {
		return Constants.PRINT_SYNTAX.equals(getSyntax());
	}

	public boolean isDebug() {
		return Constants.DEBUG_SYNTAX.equals(getSyntax());
	}

	public boolean isError() {
		return Constants.ERROR_SYNTAX.equals(getSyntax());
	}

	public abstract String getSyntax();

}

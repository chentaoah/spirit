package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.TokenEnum;

public abstract class Semantic {

	public boolean isAnnotation() {
		return TokenEnum.ANNOTATION.equals(getType());
	}

	public boolean isKeyword() {
		return TokenEnum.KEYWORD.equals(getType());
	}

	public boolean isOperator() {
		return TokenEnum.OPERATOR.equals(getType());
	}

	public boolean isSeparator() {
		return TokenEnum.SEPARATOR.equals(getType());
	}

	public boolean isType() {
		return TokenEnum.TYPE.equals(getType());
	}

	public boolean isArrayInit() {
		return TokenEnum.ARRAY_INIT.equals(getType());
	}

	public boolean isTypeInit() {
		return TokenEnum.TYPE_INIT.equals(getType());
	}

	public boolean isNull() {
		return TokenEnum.NULL.equals(getType());
	}

	public boolean isBool() {
		return TokenEnum.BOOL.equals(getType());
	}

	public boolean isChar() {
		return TokenEnum.CHAR.equals(getType());
	}

	public boolean isInt() {
		return TokenEnum.INT.equals(getType());
	}

	public boolean isLong() {
		return TokenEnum.LONG.equals(getType());
	}

	public boolean isDouble() {
		return TokenEnum.DOUBLE.equals(getType());
	}

	public boolean isStr() {
		return TokenEnum.STR.equals(getType());
	}

	public boolean isList() {
		return TokenEnum.LIST.equals(getType());
	}

	public boolean isMap() {
		return TokenEnum.MAP.equals(getType());
	}

	public boolean isSubexpress() {
		return TokenEnum.SUBEXPRESS.equals(getType());
	}

	public boolean isCast() {
		return TokenEnum.CAST.equals(getType());
	}

	public boolean isVar() {
		return TokenEnum.VAR.equals(getType());
	}

	public boolean isLocalMethod() {
		return TokenEnum.LOCAL_METHOD.equals(getType());
	}

	public boolean isVisitField() {
		return TokenEnum.VISIT_FIELD.equals(getType());
	}

	public boolean isInvokeMethod() {
		return TokenEnum.INVOKE_METHOD.equals(getType());
	}

	public boolean isVisitArrayIndex() {
		return TokenEnum.VISIT_ARRAY_INDEX.equals(getType());
	}

	public boolean isArrayIndex() {
		return TokenEnum.ARRAY_INDEX.equals(getType());
	}

	public boolean isPrefix() {
		return TokenEnum.PREFIX.equals(getType());
	}

	public boolean isNode() {
		return TokenEnum.NODE.equals(getType());
	}

	public boolean isCustomPrefix() {
		return TokenEnum.CUSTOM_PREFIX.equals(getType());
	}

	public boolean isCustomSuffix() {
		return TokenEnum.CUSTOM_SUFFIX.equals(getType());
	}

	public boolean isCustomExpress() {
		return TokenEnum.CUSTOM_EXPRESS.equals(getType());
	}

	public boolean isInit() {
		return isArrayInit() || isTypeInit();
	}

	public boolean isValue() {
		return isNull() || isBool() || isChar() || isInt() || isLong() || isDouble() || isStr() || isList() || isMap();
	}

	public boolean isNumber() {
		return isInt() || isLong() || isDouble();
	}

	public boolean isAccess() {
		return isLocalMethod() || isVisitField() || isInvokeMethod() || isVisitArrayIndex() || isArrayIndex();
	}

	public boolean isInvoke() {
		return isTypeInit() || isLocalMethod() || isInvokeMethod();
	}

	public boolean isFluent() {
		return isVisitField() || isInvokeMethod() || isVisitArrayIndex();
	}

	public boolean canSplit() {
		return isList() || isMap() || isSubexpress() || isInvoke();
	}

	public boolean isInstanceof() {
		return isKeyword() && KeywordEnum.INSTANCEOF.value.equals(getValue().toString());
	}

	public boolean isArithmetic() {
		return isOperator() && SymbolEnum.isArithmetic(getValue().toString());
	}

	public boolean isBitwise() {
		return isOperator() && SymbolEnum.isBitwise(getValue().toString());
	}

	public boolean isRelation() {
		return isOperator() && SymbolEnum.isRelation(getValue().toString());
	}

	public boolean isLogical() {
		return isOperator() && SymbolEnum.isLogical(getValue().toString());
	}

	public boolean isAssign() {
		return isOperator() && SymbolEnum.isAssign(getValue().toString());
	}

	public boolean isEquals() {
		return isOperator() && ("==".equals(getValue()) || "!=".equals(getValue()));
	}

	public boolean isShift() {
		return isOperator() && ("<<".equals(getValue()) || ">>".equals(getValue()));
	}

	public abstract TokenEnum getType();

	public abstract <T> T getValue();

}

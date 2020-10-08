package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.TokenEnum;

public abstract class Semantic {

	public boolean isAnnotation() {
		return getType() == TokenEnum.ANNOTATION;
	}

	public boolean isKeyword() {
		return getType() == TokenEnum.KEYWORD;
	}

	public boolean isOperator() {
		return getType() == TokenEnum.OPERATOR;
	}

	public boolean isSeparator() {
		return getType() == TokenEnum.SEPARATOR;
	}

	public boolean isType() {
		return getType() == TokenEnum.TYPE;
	}

	public boolean isArrayInit() {
		return getType() == TokenEnum.ARRAY_INIT;
	}

	public boolean isTypeInit() {
		return getType() == TokenEnum.TYPE_INIT;
	}

	public boolean isNull() {
		return getType() == TokenEnum.NULL;
	}

	public boolean isBool() {
		return getType() == TokenEnum.BOOL;
	}

	public boolean isChar() {
		return getType() == TokenEnum.CHAR;
	}

	public boolean isInt() {
		return getType() == TokenEnum.INT;
	}

	public boolean isLong() {
		return getType() == TokenEnum.LONG;
	}

	public boolean isDouble() {
		return getType() == TokenEnum.DOUBLE;
	}

	public boolean isStr() {
		return getType() == TokenEnum.STR;
	}

	public boolean isList() {
		return getType() == TokenEnum.LIST;
	}

	public boolean isMap() {
		return getType() == TokenEnum.MAP;
	}

	public boolean isSubexpress() {
		return getType() == TokenEnum.SUBEXPRESS;
	}

	public boolean isCast() {
		return getType() == TokenEnum.CAST;
	}

	public boolean isVar() {
		return getType() == TokenEnum.VAR;
	}

	public boolean isLocalMethod() {
		return getType() == TokenEnum.LOCAL_METHOD;
	}

	public boolean isVisitField() {
		return getType() == TokenEnum.VISIT_FIELD;
	}

	public boolean isInvokeMethod() {
		return getType() == TokenEnum.INVOKE_METHOD;
	}

	public boolean isVisitArrayIndex() {
		return getType() == TokenEnum.VISIT_ARRAY_INDEX;
	}

	public boolean isArrayIndex() {
		return getType() == TokenEnum.ARRAY_INDEX;
	}

	public boolean isPrefix() {
		return getType() == TokenEnum.PREFIX;
	}

	public boolean isNode() {
		return getType() == TokenEnum.NODE;
	}

	public boolean isCustomPrefix() {
		return getType() == TokenEnum.CUSTOM_PREFIX;
	}

	public boolean isCustomSuffix() {
		return getType() == TokenEnum.CUSTOM_SUFFIX;
	}

	public boolean isCustomExpress() {
		return getType() == TokenEnum.CUSTOM_EXPRESS;
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

	public boolean isModifier() {
		return isKeyword() && KeywordEnum.isModifier(getValue());
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

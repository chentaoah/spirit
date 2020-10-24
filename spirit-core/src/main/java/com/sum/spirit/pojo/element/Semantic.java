package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public abstract class Semantic {

	public boolean isAnnotation() {
		return getType() == TokenTypeEnum.ANNOTATION;
	}

	public boolean isKeyword() {
		return getType() == TokenTypeEnum.KEYWORD;
	}

	public boolean isOperator() {
		return getType() == TokenTypeEnum.OPERATOR;
	}

	public boolean isSeparator() {
		return getType() == TokenTypeEnum.SEPARATOR;
	}

	public boolean isType() {
		return getType() == TokenTypeEnum.TYPE;
	}

	public boolean isArrayInit() {
		return getType() == TokenTypeEnum.ARRAY_INIT;
	}

	public boolean isTypeInit() {
		return getType() == TokenTypeEnum.TYPE_INIT;
	}

	public boolean isNull() {
		return getType() == TokenTypeEnum.NULL;
	}

	public boolean isBool() {
		return getType() == TokenTypeEnum.BOOL;
	}

	public boolean isChar() {
		return getType() == TokenTypeEnum.CHAR;
	}

	public boolean isInt() {
		return getType() == TokenTypeEnum.INT;
	}

	public boolean isLong() {
		return getType() == TokenTypeEnum.LONG;
	}

	public boolean isDouble() {
		return getType() == TokenTypeEnum.DOUBLE;
	}

	public boolean isStr() {
		return getType() == TokenTypeEnum.STR;
	}

	public boolean isList() {
		return getType() == TokenTypeEnum.LIST;
	}

	public boolean isMap() {
		return getType() == TokenTypeEnum.MAP;
	}

	public boolean isSubexpress() {
		return getType() == TokenTypeEnum.SUBEXPRESS;
	}

	public boolean isCast() {
		return getType() == TokenTypeEnum.CAST;
	}

	public boolean isVar() {
		return getType() == TokenTypeEnum.VAR;
	}

	public boolean isLocalMethod() {
		return getType() == TokenTypeEnum.LOCAL_METHOD;
	}

	public boolean isVisitField() {
		return getType() == TokenTypeEnum.VISIT_FIELD;
	}

	public boolean isInvokeMethod() {
		return getType() == TokenTypeEnum.INVOKE_METHOD;
	}

	public boolean isVisitArrayIndex() {
		return getType() == TokenTypeEnum.VISIT_ARRAY_INDEX;
	}

	public boolean isArrayIndex() {
		return getType() == TokenTypeEnum.ARRAY_INDEX;
	}

	public boolean isPrefix() {
		return getType() == TokenTypeEnum.PREFIX;
	}

	public boolean isNode() {
		return getType() == TokenTypeEnum.NODE;
	}

	public boolean isCustomPrefix() {
		return getType() == TokenTypeEnum.CUSTOM_PREFIX;
	}

	public boolean isCustomSuffix() {
		return getType() == TokenTypeEnum.CUSTOM_SUFFIX;
	}

	public boolean isCustomExpress() {
		return getType() == TokenTypeEnum.CUSTOM_EXPRESS;
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

	public abstract TokenTypeEnum getType();

	public abstract <T> T getValue();

}

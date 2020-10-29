package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.pojo.enums.SymbolEnum;
import com.sum.spirit.pojo.enums.TokenTypeEnum;

public abstract class Semantic extends AttributeMap {

	public TokenTypeEnum tokenType;

	public Semantic(TokenTypeEnum tokenType) {
		this.tokenType = tokenType;
	}

	public boolean isAnnotation() {
		return tokenType == TokenTypeEnum.ANNOTATION;
	}

	public boolean isKeyword() {
		return tokenType == TokenTypeEnum.KEYWORD;
	}

	public boolean isOperator() {
		return tokenType == TokenTypeEnum.OPERATOR;
	}

	public boolean isSeparator() {
		return tokenType == TokenTypeEnum.SEPARATOR;
	}

	public boolean isType() {
		return tokenType == TokenTypeEnum.TYPE;
	}

	public boolean isArrayInit() {
		return tokenType == TokenTypeEnum.ARRAY_INIT;
	}

	public boolean isTypeInit() {
		return tokenType == TokenTypeEnum.TYPE_INIT;
	}

	public boolean isNull() {
		return tokenType == TokenTypeEnum.NULL;
	}

	public boolean isBool() {
		return tokenType == TokenTypeEnum.BOOL;
	}

	public boolean isChar() {
		return tokenType == TokenTypeEnum.CHAR;
	}

	public boolean isInt() {
		return tokenType == TokenTypeEnum.INT;
	}

	public boolean isLong() {
		return tokenType == TokenTypeEnum.LONG;
	}

	public boolean isDouble() {
		return tokenType == TokenTypeEnum.DOUBLE;
	}

	public boolean isStr() {
		return tokenType == TokenTypeEnum.STR;
	}

	public boolean isList() {
		return tokenType == TokenTypeEnum.LIST;
	}

	public boolean isMap() {
		return tokenType == TokenTypeEnum.MAP;
	}

	public boolean isSubexpress() {
		return tokenType == TokenTypeEnum.SUBEXPRESS;
	}

	public boolean isCast() {
		return tokenType == TokenTypeEnum.CAST;
	}

	public boolean isVar() {
		return tokenType == TokenTypeEnum.VAR;
	}

	public boolean isLocalMethod() {
		return tokenType == TokenTypeEnum.LOCAL_METHOD;
	}

	public boolean isVisitField() {
		return tokenType == TokenTypeEnum.VISIT_FIELD;
	}

	public boolean isInvokeMethod() {
		return tokenType == TokenTypeEnum.INVOKE_METHOD;
	}

	public boolean isVisitArrayIndex() {
		return tokenType == TokenTypeEnum.VISIT_ARRAY_INDEX;
	}

	public boolean isArrayIndex() {
		return tokenType == TokenTypeEnum.ARRAY_INDEX;
	}

	public boolean isPrefix() {
		return tokenType == TokenTypeEnum.PREFIX;
	}

	public boolean isCustomPrefix() {
		return tokenType == TokenTypeEnum.CUSTOM_PREFIX;
	}

	public boolean isCustomSuffix() {
		return tokenType == TokenTypeEnum.CUSTOM_SUFFIX;
	}

	public boolean isCustomExpress() {
		return tokenType == TokenTypeEnum.CUSTOM_EXPRESS;
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

	public abstract <T> T getValue();

}

package com.sum.spirit.core.element.frame;

import java.util.Map;

import com.sum.spirit.common.enums.AttributeEnum;
import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.SymbolEnum;
import com.sum.spirit.common.enums.TokenTypeEnum;

public abstract class Semantic extends AttributeMap {

	public TokenTypeEnum tokenType;

	public Semantic(TokenTypeEnum tokenType, Map<AttributeEnum, Object> attributes) {
		super(attributes);
		this.tokenType = tokenType;
	}

	public boolean isPath() {
		return tokenType == TokenTypeEnum.PATH;
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

	public boolean isBoolean() {
		return tokenType == TokenTypeEnum.BOOLEAN;
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

	public boolean isString() {
		return tokenType == TokenTypeEnum.STRING;
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

	public boolean isVariable() {
		return tokenType == TokenTypeEnum.VARIABLE;
	}

	public boolean isLocalMethod() {
		return tokenType == TokenTypeEnum.LOCAL_METHOD;
	}

	public boolean isVisitField() {
		return tokenType == TokenTypeEnum.VISIT_FIELD;
	}

	public boolean isVisitMethod() {
		return tokenType == TokenTypeEnum.VISIT_METHOD;
	}

	public boolean isVisitIndex() {
		return tokenType == TokenTypeEnum.VISIT_INDEX;
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

	public boolean isLiteral() {
		return isNull() || isBoolean() || isChar() || isInt() || isLong() || isDouble() || isString() || isList() || isMap();
	}

	public boolean isNumber() {
		return isInt() || isLong() || isDouble();
	}

	public boolean isAccess() {
		return isLocalMethod() || isVisitField() || isVisitMethod() || isVisitIndex();
	}

	public boolean isInvoke() {
		return isTypeInit() || isLocalMethod() || isVisitMethod();
	}

	public boolean isVisit() {
		return isVisitField() || isVisitMethod() || isVisitIndex();
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
		return isOperator() && SymbolEnum.EQUAL.value.equals(getValue());
	}

	public boolean isUnequals() {
		return isOperator() && SymbolEnum.UNEQUAL.value.equals(getValue());
	}

	public boolean isShift() {
		return isOperator() && (SymbolEnum.LEFT_SHIFT.value.equals(getValue()) || SymbolEnum.RIGHT_SHIFT.value.equals(getValue()));
	}

	public boolean isNegate() {
		return isOperator() && SymbolEnum.NEGATE.value.equals(getValue());
	}

	public boolean isLogicAnd() {
		return isOperator() && SymbolEnum.AND.value.equals(getValue());
	}

	public boolean isLogicOr() {
		return isOperator() && SymbolEnum.OR.value.equals(getValue());
	}

	public abstract <T> T getValue();

}

package com.sum.slimx.pojo.element;

import com.sum.slimx.pojo.common.Constants;
import com.sum.slimx.pojo.common.SymbolTable;

public abstract class Semantic {

	public boolean isAnnotation() {
		return Constants.ANNOTATION_TOKEN.equals(getType());
	}

	public boolean isKeyword() {
		return Constants.KEYWORD_TOKEN.equals(getType());
	}

	public boolean isOperator() {
		return Constants.OPERATOR_TOKEN.equals(getType());
	}

	public boolean isSeparator() {
		return Constants.SEPARATOR_TOKEN.equals(getType());
	}

	public boolean isType() {
		return Constants.TYPE_TOKEN.equals(getType());
	}

	public boolean isArrayInit() {
		return Constants.ARRAY_INIT_TOKEN.equals(getType());
	}

	public boolean isTypeInit() {
		return Constants.TYPE_INIT_TOKEN.equals(getType());
	}

	public boolean isNull() {
		return Constants.NULL_TOKEN.equals(getType());
	}

	public boolean isBool() {
		return Constants.BOOL_TOKEN.equals(getType());
	}

	public boolean isChar() {
		return Constants.CHAR_TOKEN.equals(getType());
	}

	public boolean isInt() {
		return Constants.INT_TOKEN.equals(getType());
	}

	public boolean isLong() {
		return Constants.LONG_TOKEN.equals(getType());
	}

	public boolean isDouble() {
		return Constants.DOUBLE_TOKEN.equals(getType());
	}

	public boolean isStr() {
		return Constants.STR_TOKEN.equals(getType());
	}

	public boolean isList() {
		return Constants.LIST_TOKEN.equals(getType());
	}

	public boolean isMap() {
		return Constants.MAP_TOKEN.equals(getType());
	}

	public boolean isSubexpress() {
		return Constants.SUBEXPRESS_TOKEN.equals(getType());
	}

	public boolean isCast() {
		return Constants.CAST_TOKEN.equals(getType());
	}

	public boolean isVar() {
		return Constants.VAR_TOKEN.equals(getType());
	}

	public boolean isLocalMethod() {
		return Constants.LOCAL_METHOD_TOKEN.equals(getType());
	}

	public boolean isVisitField() {
		return Constants.VISIT_FIELD_TOKEN.equals(getType());
	}

	public boolean isInvokeMethod() {
		return Constants.INVOKE_METHOD_TOKEN.equals(getType());
	}

	public boolean isVisitArrayIndex() {
		return Constants.VISIT_ARRAY_INDEX_TOKEN.equals(getType());
	}

	public boolean isArrayIndex() {
		return Constants.ARRAY_INDEX_TOKEN.equals(getType());
	}

	public boolean isPrefix() {
		return Constants.PREFIX_TOKEN.equals(getType());
	}

	public boolean isNode() {
		return Constants.NODE_TOKEN.equals(getType());
	}

	public boolean isCustomPrefix() {
		return Constants.CUSTOM_PREFIX_TOKEN.equals(getType());
	}

	public boolean isCustomSuffix() {
		return Constants.CUSTOM_SUFFIX_TOKEN.equals(getType());
	}

	public boolean isCustomExpress() {
		return Constants.CUSTOM_EXPRESS_TOKEN.equals(getType());
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
		return isKeyword() && Constants.INSTANCEOF_KEYWORD.equals(getValue().toString());
	}

	public boolean isArithmetic() {
		return isOperator() && SymbolTable.isArithmetic(getValue().toString());
	}

	public boolean isBitwise() {
		return isOperator() && SymbolTable.isBitwise(getValue().toString());
	}

	public boolean isRelation() {
		return isOperator() && SymbolTable.isRelation(getValue().toString());
	}

	public boolean isLogical() {
		return isOperator() && SymbolTable.isLogical(getValue().toString());
	}

	public boolean isAssign() {
		return isOperator() && SymbolTable.isAssign(getValue().toString());
	}

	public boolean isEquals() {
		return isOperator() && ("==".equals(getValue()) || "!=".equals(getValue()));
	}

	public boolean isShift() {
		return isOperator() && ("<<".equals(getValue()) || ">>".equals(getValue()));
	}

	public abstract String getType();

	public abstract <T> T getValue();

}

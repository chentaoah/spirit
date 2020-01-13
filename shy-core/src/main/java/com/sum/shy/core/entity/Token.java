package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.Map;

import com.sum.shy.type.api.Type;

public class Token {

	public String type;

	public Object value;

	public Map<String, Object> attachments;// 解析获得的其他信息

	public Token() {
		attachments = new HashMap<>();
	}

	public Token(String type, Object value, Map<String, Object> attachments) {
		this.type = type;
		this.value = value;
		this.attachments = attachments == null ? new HashMap<>() : attachments;
	}

	public Token(String type, Object value) {
		this.type = type;
		this.value = value;
		this.attachments = new HashMap<>();
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public String debug() {
		return "<" + type + ", " + value + ">";
	}

	public boolean isKeyword() {
		return Constants.KEYWORD_TOKEN.equals(type);
	}

	public boolean isKeywordParam() {
		return Constants.KEYWORD_PARAM_TOKEN.equals(type);
	}

	public boolean isOperator() {
		return Constants.OPERATOR_TOKEN.equals(type);
	}

	public boolean isSeparator() {
		return Constants.SEPARATOR_TOKEN.equals(type);
	}

	public boolean isType() {
		return Constants.TYPE_TOKEN.equals(type);
	}

	public boolean isArrayInit() {
		return Constants.ARRAY_INIT_TOKEN.equals(type);
	}

	public boolean isTypeInit() {
		return Constants.TYPE_INIT_TOKEN.equals(type);
	}

	public boolean isNull() {
		return Constants.NULL_TOKEN.equals(type);
	}

	public boolean isBool() {
		return Constants.BOOL_TOKEN.equals(type);
	}

	public boolean isInt() {
		return Constants.INT_TOKEN.equals(type);
	}

	public boolean isDouble() {
		return Constants.DOUBLE_TOKEN.equals(type);
	}

	public boolean isStr() {
		return Constants.STR_TOKEN.equals(type);
	}

	public boolean isList() {
		return Constants.LIST_TOKEN.equals(type);
	}

	public boolean isMap() {
		return Constants.MAP_TOKEN.equals(type);
	}

	public boolean isSubexpress() {
		return Constants.SUBEXPRESS_TOKEN.equals(type);
	}

	public boolean isCast() {
		return Constants.CAST_TOKEN.equals(type);
	}

	public boolean isVar() {
		return Constants.VAR_TOKEN.equals(type);
	}

	public boolean isInvokeLocal() {
		return Constants.INVOKE_LOCAL_TOKEN.equals(type);
	}

	public boolean isVisitField() {
		return Constants.VISIT_FIELD_TOKEN.equals(type);
	}

	public boolean isInvokeMethod() {
		return Constants.INVOKE_METHOD_TOKEN.equals(type);
	}

	public boolean isVisitArrayIndex() {
		return Constants.VISIT_ARRAY_INDEX_TOKEN.equals(type);
	}

	public boolean isArrayIndex() {
		return Constants.ARRAY_INDEX_TOKEN.equals(type);
	}

	public boolean isInstanceof() {
		return isKeyword() && "instanceof".equals(value.toString());
	}

	public boolean isPrefix() {
		return Constants.PREFIX_TOKEN.equals(type);
	}

	public boolean isNode() {
		return Constants.NODE_TOKEN.equals(type);
	}

	public boolean isExpress() {
		return Constants.EXPRESS_TOKEN.equals(type);
	}

	public boolean isUnknown() {
		return Constants.UNKNOWN.equals(type);
	}

	// =================== 复合判断 =====================

	public boolean isInit() {
		return isArrayInit() || isTypeInit();
	}

	public boolean isValue() {
		return isNull() || isBool() || isInt() || isDouble() || isStr() || isList() || isMap();
	}

	public boolean isAccess() {
		return isInvokeLocal() || isVisitField() || isInvokeMethod() || isVisitArrayIndex() || isArrayIndex();
	}

	public boolean isInvoke() {
		return isTypeInit() || isInvokeLocal() || isInvokeMethod();
	}

	public boolean isFluent() {
		return isVisitField() || isInvokeMethod() || isVisitArrayIndex();
	}

	public boolean hasSubStmt() {
		return isList() || isMap() || isSubexpress() || isInvoke();
	}

	public boolean isLogicalOperator() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		if (isOperator())
			return "!".equals(value) || "&&".equals(value) || "||".equals(value);
		return false;
	}

	public boolean isJudgeOperator() {
		if (isOperator())
			return "==".equals(value) || "!=".equals(value) || ">=".equals(value) || "<=".equals(value)
					|| ">".equals(value) || "<".equals(value);
		return false;
	}

	public boolean isEqualsOperator() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		if (isOperator())
			return "==".equals(value) || "!=".equals(value);
		return false;
	}

	public boolean isCalcOperator() {
		if (isOperator())
			return "++".equals(value) || "--".equals(value) || "+".equals(value) || "-".equals(value)
					|| "*".equals(value) || "/".equals(value) || "%".equals(value);
		return false;
	}

	// =================== 类型 =====================

	public Type getTypeAtt() {
		return (Type) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(Type type) {
		if (type == null)
			throw new RuntimeException("Type cannot be null!token:" + this.toString());
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	// =================== 类名 =====================

	public String getTypeNameAtt() {
		return (String) attachments.get(Constants.TYPE_NAME_ATTACHMENT);
	}

	public void setTypeNameAtt(String str) {
		attachments.put(Constants.TYPE_NAME_ATTACHMENT, str);
	}

	// =================== 成员名称 =====================

	public String getMemberNameAtt() {
		return (String) attachments.get(Constants.MEMBER_NAME_ATTACHMENT);
	}

	public void setMemberNameAtt(String str) {
		attachments.put(Constants.MEMBER_NAME_ATTACHMENT, str);
	}

	// =================== 是否已经被声明 =====================

	public boolean isDeclaredAtt() {
		Object flag = attachments.get(Constants.IS_DECLARED_ATTACHMENT);
		return flag != null ? (boolean) flag : true;// 默认返回true
	}

	public void setDeclaredAtt(boolean isDeclared) {
		attachments.put(Constants.IS_DECLARED_ATTACHMENT, isDeclared);
	}

	// =================== 在语句中的位置 =====================

	public int getPosition() {
		return (int) attachments.get(Constants.POSITION_ATTACHMENT);
	}

	public void setPosition(int position) {
		attachments.put(Constants.POSITION_ATTACHMENT, position);
	}

}

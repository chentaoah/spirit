package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.Map;

public class Token {

	public String type;

	public Object value;

	public Map<String, String> attachments;// 解析获得的其他信息

	public Token(String type, Object value, Map<String, String> attachments) {
		this.type = type;
		this.value = value;
		this.attachments = attachments == null ? new HashMap<>() : attachments;
	}

	@Override
	public String toString() {
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

	public boolean isNull() {
		return Constants.NULL_TOKEN.equals(type);
	}

	public boolean isBoolean() {
		return Constants.BOOLEAN_TOKEN.equals(type);
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

	public boolean isArray() {
		return Constants.ARRAY_TOKEN.equals(type);
	}

	public boolean isMap() {
		return Constants.MAP_TOKEN.equals(type);
	}

	public boolean isInvoke() {
		return isInvokeInit() || isInvokeStatic() || isInvokeMember();
	}

	public boolean isInvokeInit() {
		return Constants.INVOKE_INIT_TOKEN.equals(type);
	}

	public boolean isInvokeStatic() {
		return Constants.INVOKE_STATIC_TOKEN.equals(type);
	}

	public boolean isInvokeMember() {
		return Constants.INVOKE_MEMBER_TOKEN.equals(type);
	}

	public boolean isVar() {
		return Constants.VAR_TOKEN.equals(type);
	}

	public boolean isStaticVar() {
		return Constants.STATIC_VAR_TOKEN.equals(type);
	}

	public boolean isMemberVar() {
		return Constants.MEMBER_VAR_TOKEN.equals(type);
	}

	public boolean isInvokeName() {
		return Constants.INVOKE_NAME_TOKEN.equals(type);
	}

	public boolean isType() {
		return Constants.TYPE_TOKEN.equals(type);
	}

	public boolean isUnknown() {
		return Constants.UNKNOWN.equals(type);
	}

	public boolean hasSubStmt() {
		return isArray() || isMap() || isInvoke();
	}

	public String getTypeAttachment() {
		return attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAttachment(String type) {
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	public String getInitMethodNameAttachment() {
		return attachments.get(Constants.INIT_METHOD_NAME_ATTACHMENT);
	}

	public String getVarNameAttachment() {
		return attachments.get(Constants.VAR_NAME_ATTACHMENT);
	}

}

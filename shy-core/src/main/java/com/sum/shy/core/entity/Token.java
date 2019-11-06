package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.Map;

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

	public boolean isInvokeLocal() {
		return Constants.INVOKE_LOCAL_TOKEN.equals(type);
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

	public boolean isCast() {
		return Constants.CAST_TOKEN.equals(type);
	}

	public boolean isPrefix() {
		return Constants.PREFIX_TOKEN.equals(type);
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

	// =================== get/set方法 =====================

	public Type getTypeAtt() {
		return (Type) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(Type type) {
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	public Type getReturnTypeAtt() {
		return (Type) attachments.get(Constants.RETURN_TYPE_ATTACHMENT);
	}

	public void setReturnTypeAtt(Type type) {
		attachments.put(Constants.RETURN_TYPE_ATTACHMENT, type);
	}

	public String getInitMethodNameAtt() {
		return (String) attachments.get(Constants.INIT_METHOD_NAME_ATTACHMENT);
	}

	public void setInitMethodNameAtt(String str) {
		attachments.put(Constants.INIT_METHOD_NAME_ATTACHMENT, str);
	}

	public String getClassNameAtt() {
		return (String) attachments.get(Constants.CLASS_NAME_ATTACHMENT);
	}

	public void setClassNameAtt(String str) {
		attachments.put(Constants.CLASS_NAME_ATTACHMENT, str);
	}

	public String getVarNameAtt() {
		return (String) attachments.get(Constants.VAR_NAME_ATTACHMENT);
	}

	public void setVarNameAtt(String str) {
		attachments.put(Constants.VAR_NAME_ATTACHMENT, str);
	}

	public String getStaticMethodNameAtt() {
		return (String) attachments.get(Constants.STATIC_METHOD_NAME_ATTACHMENT);
	}

	public void setStaticMethodNameAtt(String str) {
		attachments.put(Constants.STATIC_METHOD_NAME_ATTACHMENT, str);
	}

	public String getMemberMethodNameAtt() {
		return (String) attachments.get(Constants.MEMBER_METHOD_NAME_ATTACHMENT);
	}

	public void setMemberMethodNameAtt(String str) {
		attachments.put(Constants.MEMBER_METHOD_NAME_ATTACHMENT, str);
	}

	public String getLocalMethodNameAtt() {
		return (String) attachments.get(Constants.LOCAL_METHOD_NAME_ATTACHMENT);
	}

	public void setLocalMethodNameAtt(Object str) {
		attachments.put(Constants.LOCAL_METHOD_NAME_ATTACHMENT, str);
	}

	public String getMemberVarNameAtt() {
		return (String) attachments.get(Constants.MEMBER_VAR_NAME_ATTACHMENT);
	}

	public void setMemberVarNameAtt(String str) {
		attachments.put(Constants.MEMBER_VAR_NAME_ATTACHMENT, str);

	}

}

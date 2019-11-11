package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.List;
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
		return isInvokeInit() || isInvokeStatic() || isInvokeMember() || isInvokeLocal() || isInvokeFluent();
	}

	public boolean isFluent() {
		return isInvokeFluent() || isMemberVarFluent();
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

	public boolean isInvokeFluent() {
		return Constants.INVOKE_FLUENT_TOKEN.equals(type);
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

	public boolean isMemberVarFluent() {
		return Constants.MEMBER_VAR_FLUENT_TOKEN.equals(type);
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

	public NativeType getNativeTypeAtt() {
		return (NativeType) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setNativeTypeAtt(NativeType type) {
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	public NativeType getReturnNativeTypeAtt() {
		return (NativeType) attachments.get(Constants.RETURN_TYPE_ATTACHMENT);
	}

	public void setReturnNativeTypeAtt(NativeType type) {
		attachments.put(Constants.RETURN_TYPE_ATTACHMENT, type);
	}

	// =================== 类名 =====================

	public String getClassNameAtt() {
		return (String) attachments.get(Constants.CLASS_NAME_ATTACHMENT);
	}

	public void setClassNameAtt(String str) {
		attachments.put(Constants.CLASS_NAME_ATTACHMENT, str);
	}

	// =================== 变量名 =====================

	public String getVarNameAtt() {
		return (String) attachments.get(Constants.VAR_NAME_ATTACHMENT);
	}

	public void setVarNameAtt(String str) {
		attachments.put(Constants.VAR_NAME_ATTACHMENT, str);
	}

	// =================== 后缀名 =====================

	@SuppressWarnings("unchecked")
	public List<String> getVarNamesAtt() {
		return (List<String>) attachments.get(Constants.VAR_NAMES_ATTACHMENT);
	}

	public void setVarNamesAtt(List<String> list) {
		attachments.put(Constants.VAR_NAMES_ATTACHMENT, list);
	}

	// =================== 方法名 =====================

	public String getMethodNameAtt() {
		return (String) attachments.get(Constants.VAR_NAME_ATTACHMENT);
	}

	public void setMethodNameAtt(String str) {
		attachments.put(Constants.METHOD_NAME_ATTACHMENT, str);
	}

	// =================== 类型转换 =====================

	public String getCastTypeAtt() {
		return (String) attachments.get(Constants.CAST_TYPE_ATTACHMENT);
	}

	public void setCastTypeAtt(String castType) {
		attachments.put(Constants.CAST_TYPE_ATTACHMENT, castType);
	}

	// =================== 下一个的引用 =====================

	public Token getNextTokenAtt() {
		return (Token) attachments.get(Constants.NEXT_TOKEN_ATTACHMENT);
	}

	public void setNextTokenAtt(Token nextToken) {
		attachments.put(Constants.NEXT_TOKEN_ATTACHMENT, nextToken);
	}

}

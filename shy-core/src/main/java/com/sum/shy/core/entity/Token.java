package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Type;

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

	public boolean isType() {
		return Constants.TYPE_TOKEN.equals(type);
	}

	public boolean isArrayInit() {
		return Constants.ARRAY_INIT_TOKEN.equals(type);
	}

	public boolean isCast() {
		return Constants.CAST_TOKEN.equals(type);
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

	public boolean isArray() {
		return Constants.ARRAY_TOKEN.equals(type);
	}

	public boolean isMap() {
		return Constants.MAP_TOKEN.equals(type);
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

	public boolean isQuickIndex() {
		return Constants.QUICK_INDEX_TOKEN.equals(type);
	}

	public boolean isPrefix() {
		return Constants.PREFIX_TOKEN.equals(type);
	}

	public boolean isUnknown() {
		return Constants.UNKNOWN.equals(type);
	}

	// =================== 复合判断 =====================

	public boolean isValue() {
		return isNull() || isBool() || isInt() || isDouble() || isStr() || isArray() || isMap();
	}

	public boolean isInvoke() {
		return isInvokeInit() || isInvokeStatic() || isInvokeMember() || isInvokeLocal() || isInvokeFluent();
	}

	public boolean isVariable() {
		return isVar() || isStaticVar() || isMemberVar() || isMemberVarFluent();
	}

	public boolean isFluent() {
		return isInvokeFluent() || isMemberVarFluent();
	}

	public boolean hasSubStmt() {
		return isArray() || isMap() || isInvoke();
	}

	public boolean isJudgeOperator() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		if (isOperator())
			return "==".equals(value) || "!=".equals(value) || ">=".equals(value) || "<=".equals(value)
					|| ">".equals(value) || "<".equals(value);
		return false;
	}

	// =================== get/set方法 =====================

	public Type getTypeAtt() {
		return (Type) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(Type type) {
		if (type == null)
			throw new RuntimeException("Type cannot be null!token:" + this.toString());
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	public Type getReturnTypeAtt() {
		return (Type) attachments.get(Constants.RETURN_TYPE_ATTACHMENT);
	}

	public void setReturnTypeAtt(Type returnType) {
		if (returnType == null)
			throw new RuntimeException("Return type cannot be null!token:" + this.toString());
		attachments.put(Constants.RETURN_TYPE_ATTACHMENT, returnType);
	}

	// =================== 全名 =====================

	public String getClassNameAtt() {
		return (String) attachments.get(Constants.CLASS_NAME_ATTACHMENT);
	}

	public void setClassNameAtt(String str) {
		attachments.put(Constants.CLASS_NAME_ATTACHMENT, str);
	}

	// =================== 类名 =====================

	public String getTypeNameAtt() {
		return (String) attachments.get(Constants.TYPE_NAME_ATTACHMENT);
	}

	public void setTypeNameAtt(String str) {
		attachments.put(Constants.TYPE_NAME_ATTACHMENT, str);
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
	public List<String> getPropertiesAtt() {
		return (List<String>) attachments.get(Constants.PROPERTIES_ATTACHMENT);
	}

	public void setPropertiesAtt(List<String> list) {
		attachments.put(Constants.PROPERTIES_ATTACHMENT, list);
	}

	// =================== 方法名 =====================

	public String getMethodNameAtt() {
		return (String) attachments.get(Constants.METHOD_NAME_ATTACHMENT);
	}

	public void setMethodNameAtt(String str) {
		attachments.put(Constants.METHOD_NAME_ATTACHMENT, str);
	}

	// =================== 是否已经被声明 =====================

	public boolean isDeclaredAtt() {
		Object flag = attachments.get(Constants.IS_DECLARED_ATTACHMENT);
		return flag != null ? (boolean) flag : true;// 默认返回true
	}

	public void setDeclaredAtt(boolean isDeclared) {
		attachments.put(Constants.IS_DECLARED_ATTACHMENT, isDeclared);
	}

	// =================== 下一个token的引用 =====================

	public Token getNext() {
		return (Token) attachments.get(Constants.NEXT_TOKEN_ATTACHMENT);
	}

	public void setNext(Token token) {
		attachments.put(Constants.NEXT_TOKEN_ATTACHMENT, token);
	}

}

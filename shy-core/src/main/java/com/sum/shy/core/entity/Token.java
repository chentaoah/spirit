package com.sum.shy.core.entity;

import java.util.HashMap;
import java.util.Map;

import com.sum.shy.core.type.api.Type;

public class Token {

	public String type;

	public Object value;

	public Map<String, Object> attachments;// 解析获得的其他信息

	public Token() {
		attachments = new HashMap<>();
	}

	public Token(Object value) {
		this.value = value;
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

	public boolean isVisitMember() {
		return Constants.VISIT_MEMBER_TOKEN.equals(type);
	}

	public boolean isInvokeMember() {
		return Constants.INVOKE_MEMBER_TOKEN.equals(type);
	}

	public boolean isQuickIndex() {
		return Constants.QUICK_INDEX_TOKEN.equals(type);
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

	public boolean isUnknown() {
		return Constants.UNKNOWN.equals(type);
	}

	// =================== 复合判断 =====================

	public boolean isValue() {
		return isNull() || isBool() || isInt() || isDouble() || isStr() || isList() || isMap();
	}

	public boolean isInvoke() {
		return isTypeInit() || isInvokeLocal() || isInvokeMember();
	}

	public boolean isVisit() {
		return isVisitMember() || isInvokeMember();
	}

	public boolean hasSubStmt() {
		return isList() || isMap() || isInvoke() || isSubexpress();
	}

	public boolean isLogicalOperator() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		if (isOperator())
			return "!".equals(value) || "==".equals(value) || "!=".equals(value) || ">=".equals(value)
					|| "<=".equals(value) || ">".equals(value) || "<".equals(value) || "&&".equals(value)
					|| "||".equals(value);
		return false;
	}

	public boolean isCalculateOperator() {
		if (isOperator())
			return "++".equals(value) || "--".equals(value) || "+".equals(value) || "-".equals(value)
					|| "*".equals(value) || "/".equals(value) || "%".equals(value);
		return false;
	}

	// =================== 变量的类型 =====================

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

	// =================== 对语句的引用 =====================

	public Stmt getStmt() {
		return (Stmt) attachments.get(Constants.STMT_ATTACHMENT);
	}

	public void setStmt(Stmt stmt) {
		attachments.put(Constants.STMT_ATTACHMENT, stmt);
	}

}

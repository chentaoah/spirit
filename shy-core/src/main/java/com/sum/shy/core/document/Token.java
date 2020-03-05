package com.sum.shy.core.document;

import java.util.HashMap;
import java.util.Map;

import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.metadata.SymbolTable;
import com.sum.shy.core.type.api.IType;

public class Token {

	public String type;

	public Object value;

	public Map<String, Object> attachments;// 解析获得的其他信息

	public Token() {
		attachments = new HashMap<>();
	}

	public Token(String type, Object value) {
		this.type = type;
		this.value = value;
		this.attachments = new HashMap<>();
	}

	public Token(String type, Object value, Map<String, Object> attachments) {
		this.type = type;
		this.value = value;
		if (attachments == null)
			throw new RuntimeException("Please use another construction method Token(String type, Object value)");
		this.attachments = attachments == null ? new HashMap<>() : attachments;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public Stmt getStmt() {
		return (Stmt) value;
	}

	public Node getNode() {
		return (Node) value;
	}

	public String debug() {
		return "<" + type + ", " + value + ">";
	}

	public boolean isAnnotation() {
		return Constants.ANNOTATION_TOKEN.equals(type);
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

	public boolean isLong() {
		return Constants.LONG_TOKEN.equals(type);
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

	public boolean isLocalMethod() {
		return Constants.LOCAL_METHOD_TOKEN.equals(type);
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

	public boolean isPrefix() {
		return Constants.PREFIX_TOKEN.equals(type);
	}

	public boolean isNode() {
		return Constants.NODE_TOKEN.equals(type);
	}

	public boolean isUnknown() {
		return Constants.UNKNOWN.equals(type);
	}

	public boolean isCustomExpress() {
		return Constants.CUSTOM_EXPRESS_TOKEN.equals(type);
	}

	// =================== 复合判断 =====================

	public boolean isInit() {
		return isArrayInit() || isTypeInit();
	}

	public boolean isValue() {
		return isNull() || isBool() || isInt() || isLong() || isDouble() || isStr() || isList() || isMap();
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

	public boolean hasSubStmt() {
		return isList() || isMap() || isSubexpress() || isInvoke();
	}

	public boolean isInstanceof() {
		return isKeyword() && "instanceof".equals(value.toString());
	}

	public boolean isArithmetic() {
		return isOperator() && SymbolTable.isArithmetic(value.toString());
	}

	public boolean isBitwise() {// 是否按位操作符
		return isOperator() && SymbolTable.isBitwise(value.toString());
	}

	public boolean isRelation() {// 关系运算符
		return isOperator() && SymbolTable.isRelation(value.toString());
	}

	public boolean isLogical() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		return isOperator() && SymbolTable.isLogical(value.toString());
	}

	public boolean isAssign() {// 是否赋值操作符
		return isOperator() && SymbolTable.isAssign(value.toString());
	}

	public boolean isEquals() {// 是否判断的操作符,这些符号都会将value转化成boolean类型
		return isOperator() && ("==".equals(value) || "!=".equals(value));
	}

	public boolean isShift() {// 是否位移操作符
		return isOperator() && ("<<".equals(value) || ">>".equals(value));
	}

	// =================== 类型 =====================

	public IType getTypeAtt() {
		return (IType) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(IType type) {
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
package com.sum.shy.document.pojo;

import java.util.HashMap;
import java.util.Map;

import com.sum.shy.clazz.pojo.IType;
import com.sum.shy.core.pojo.Constants;
import com.sum.shy.document.pojo.api.Semantic;
import com.sum.shy.lib.Assert;

public class Token extends Semantic {

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
		this.attachments = attachments == null ? new HashMap<>() : attachments;
	}

	public Token copy() {// 拷贝内容，但是是一个新的实例
		return canVisit() ? new Token(type, getStmt().copy(), attachments) : new Token(type, value, attachments);
	}

	public Stmt getStmt() {
		return (Stmt) value;
	}

	public Node getNode() {
		return (Node) value;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public String debug() {
		return "<" + type + ", " + value + ">";
	}

	// =================== 类型 =====================

	public IType getTypeAtt() {
		return (IType) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(IType type) {
		Assert.notNull(type, "Type cannot be null!token:" + this.toString());
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	// =================== 类名 =====================

	public String getSimpleNameAtt() {
		return (String) attachments.get(Constants.SIMPLE_NAME_ATTACHMENT);
	}

	public void setSimpleNameAtt(String str) {
		attachments.put(Constants.SIMPLE_NAME_ATTACHMENT, str);
	}

	// =================== 成员名称 =====================

	public String getMemberNameAtt() {
		return (String) attachments.get(Constants.MEMBER_NAME_ATTACHMENT);
	}

	public void setMemberNameAtt(String str) {
		attachments.put(Constants.MEMBER_NAME_ATTACHMENT, str);
	}

	// =================== 是否推导得来 =====================

	public boolean isDerivedAtt() {
		Object flag = attachments.get(Constants.IS_DERIVED_ATTACHMENT);
		return flag != null ? (boolean) flag : false;
	}

	public void setDerivedAtt(boolean isDerived) {
		attachments.put(Constants.IS_DERIVED_ATTACHMENT, isDerived);
	}

	// =================== 在语句中的位置 =====================

	public int getPosition() {
		return (int) attachments.get(Constants.POSITION_ATTACHMENT);
	}

	public void setPosition(int position) {
		attachments.put(Constants.POSITION_ATTACHMENT, position);
	}

	// =================== 在语法树中的位置 =====================

	public String getTreeId() {
		return (String) attachments.get(Constants.TREE_ID_ATTACHMENT);
	}

	public void setTreeId(String treeId) {
		attachments.put(Constants.TREE_ID_ATTACHMENT, treeId);
	}

	// =================== 操作符操作数 =====================

	public int getOperand() {
		return (Integer) attachments.get(Constants.OPERAND_ATTACHMENT);
	}

	public void setOperand(int operand) {
		attachments.put(Constants.OPERAND_ATTACHMENT, operand);
	}

}

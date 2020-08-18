package com.sum.jass.pojo.element;

import java.util.HashMap;
import java.util.Map;

import com.sum.jass.lib.Assert;
import com.sum.jass.pojo.clazz.IType;
import com.sum.jass.pojo.common.Constants;

public class Token extends Semantic {

	public String type;

	public Object value;

	public Map<String, Object> attachments = new HashMap<>();

	public Token() {
	}

	public Token(String type, Object value) {
		this.type = type;
		this.value = value;
	}

	public Token(String type, Object value, Map<String, Object> attachments) {
		this.type = type;
		this.value = value;
		this.attachments = attachments != null ? attachments : this.attachments;
	}

	public Token copy() {
		return canSplit() ? new Token(type, ((Statement) getValue()).copy(), attachments) : new Token(type, value, attachments);
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getValue() {
		return (T) value;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public String debug() {
		return "<" + type + ", " + value + ">";
	}

	public String getSimpleName() {
		return (String) attachments.get(Constants.SIMPLE_NAME_ATTACHMENT);
	}

	public void setSimpleName(String str) {
		attachments.put(Constants.SIMPLE_NAME_ATTACHMENT, str);
	}

	public String getMemberName() {
		return (String) attachments.get(Constants.MEMBER_NAME_ATTACHMENT);
	}

	public void setMemberName(String str) {
		attachments.put(Constants.MEMBER_NAME_ATTACHMENT, str);
	}

	public int getOperand() {
		Integer operand = (Integer) attachments.get(Constants.OPERAND_ATTACHMENT);
		return operand != null ? operand : -1;
	}

	public void setOperand(int operand) {
		attachments.put(Constants.OPERAND_ATTACHMENT, operand);
	}

	public String getTreeId() {
		return (String) attachments.get(Constants.TREE_ID_ATTACHMENT);
	}

	public void setTreeId(String treeId) {
		attachments.put(Constants.TREE_ID_ATTACHMENT, treeId);
	}

	public IType getTypeAtt() {
		return (IType) attachments.get(Constants.TYPE_ATTACHMENT);
	}

	public void setTypeAtt(IType type) {
		Assert.notNull(type, "Type cannot be null!token:" + this.toString());
		attachments.put(Constants.TYPE_ATTACHMENT, type);
	}

	public boolean isDerived() {
		Boolean isDerived = (Boolean) attachments.get(Constants.IS_DERIVED_ATTACHMENT);
		return isDerived != null ? isDerived : false;
	}

	public void setDerived(boolean isDerived) {
		attachments.put(Constants.IS_DERIVED_ATTACHMENT, isDerived);
	}

	public int getPosition() {
		return (int) attachments.get(Constants.POSITION_ATTACHMENT);
	}

	public void setPosition(int position) {
		attachments.put(Constants.POSITION_ATTACHMENT, position);
	}

}

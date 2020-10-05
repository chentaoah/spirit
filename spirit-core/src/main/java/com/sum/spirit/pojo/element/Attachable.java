package com.sum.spirit.pojo.element;

import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.enums.SymbolEnum.OperandEnum;
import com.sum.spirit.utils.Holder;

public abstract class Attachable extends Semantic {

	public String simpleName;
	public String memberName;
	public OperandEnum operand;
	public Holder<String> treeId = new Holder<String>();
	public IType typeAtt;
	public boolean isDerived;
	public int position;

	public void copyAtt(Attachable attachable) {
		setSimpleName(attachable.getSimpleName());
		setMemberName(attachable.getMemberName());
		setOperand(attachable.getOperand());
		setTreeId(attachable.getTreeId());
		setTypeAtt(attachable.getTypeAtt());
		setDerived(attachable.isDerived());
		setPosition(attachable.getPosition());
	}

	public String getSimpleName() {
		return simpleName;
	}

	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public OperandEnum getOperand() {
		return operand;
	}

	public void setOperand(OperandEnum operand) {
		this.operand = operand;
	}

	public Holder<String> getTreeId() {
		return treeId;
	}

	public void setTreeId(Holder<String> treeId) {
		this.treeId = treeId;
	}

	public IType getTypeAtt() {
		return typeAtt;
	}

	public void setTypeAtt(IType typeAtt) {
		this.typeAtt = typeAtt;
	}

	public boolean isDerived() {
		return isDerived;
	}

	public void setDerived(boolean isDerived) {
		this.isDerived = isDerived;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}

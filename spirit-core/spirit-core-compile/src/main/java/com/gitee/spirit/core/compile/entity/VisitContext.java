package com.gitee.spirit.core.compile.entity;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.core.clazz.entity.IClass;
import com.gitee.spirit.core.clazz.entity.IField;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.entity.IVariable;
import com.gitee.spirit.core.clazz.frame.MemberEntity;
import com.google.common.base.Joiner;

public class VisitContext {

	public IClass clazz;
	public MemberEntity member;
	public List<IVariable> variables = new ArrayList<>();
	public int depth = 0;
	public List<Integer> counts = new ArrayList<>(16);
	public IType returnType;

	public VisitContext(IClass clazz, MemberEntity member) {
		this.clazz = clazz;
		this.member = member;
	}

	public boolean isFieldScope() {
		return member instanceof IField;
	}

	public boolean isMethodScope() {
		return member instanceof IMethod;
	}

	public void increaseDepth() {
		depth = depth + 1;
	}

	public void decreaseDepth() {
		depth = depth - 1;
	}

	public void increaseCount() {
		while (depth >= counts.size()) {
			counts.add(0);
		}
		counts.set(depth, counts.get(depth) + 1);
	}

	public String getBlockId() {
		while (depth >= counts.size()) {
			counts.add(0);
		}
		return Joiner.on("-").join(counts.subList(0, depth + 1));
	}

}

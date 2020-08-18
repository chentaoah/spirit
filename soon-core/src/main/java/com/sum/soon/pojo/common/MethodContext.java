package com.sum.soon.pojo.common;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.soon.pojo.clazz.IMethod;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.clazz.IVariable;

public class MethodContext {

	public IMethod method;
	public List<IVariable> variables = new ArrayList<>();
	public int depth = 0;// 深度
	public List<Integer> counts = new ArrayList<>(16);
	public IType returnType;// 返回

	public MethodContext(IMethod method) {
		this.method = method;
	}

	public void increaseDepth() {
		depth = depth + 1;
	}

	public void decreaseDepth() {
		depth = depth - 1;
	}

	public void increaseCount() {
		while (depth >= counts.size())
			counts.add(0);
		counts.set(depth, counts.get(depth) + 1);
	}

	public String getBlockId() {
		while (depth >= counts.size())
			counts.add(0);
		return Joiner.on("-").join(counts.subList(0, depth + 1));
	}

}

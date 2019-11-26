package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.core.api.Type;

public class CtMethod extends AbsElement {
	// 参数名
	public String name;
	// 初始值
	public List<Param> params;
	// 变量
	public List<Variable> variables = new ArrayList<>();
	// method域
	public List<Line> methodLines;
	// 锁
	public volatile boolean isLock = false;

	public CtMethod(Type returnType, String name, List<Param> params) {
		this.type = returnType;
		this.name = name;
		this.params = params;
	}

	@Override
	public String toString() {
		return "method --> " + name + "(" + Joiner.on(", ").join(params) + ")";
	}

	public Variable findVariable(String block, String name) {
		for (Variable variable : variables) {
			if (block.startsWith(variable.block) && variable.name.equals(name)) {
				return variable;
			}
		}
		return null;

	}

	public void addVariable(Variable variable) {
		variables.add(variable);
	}

}

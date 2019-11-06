package com.sum.shy.core.entity;

import java.util.ArrayList;
import java.util.List;

public class Method {
	// 类型
	public Type returnType;
	// 参数名
	public String name;
	// 初始值
	public List<Param> params;
	// 变量
	public List<Variable> variables = new ArrayList<>();
	// method域
	public List<Line> methodLines;

	public Method(Type returnType, String name, List<Param> params) {
		this.returnType = returnType;
		this.name = name;
		this.params = params;
	}

	public Method(String type, String name, List<Param> params) {
		this.returnType = new Type(type);
		this.name = name;
		this.params = params;
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

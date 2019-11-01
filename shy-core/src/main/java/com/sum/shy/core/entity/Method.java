package com.sum.shy.core.entity;

import java.util.List;

public class Method {
	// 类型
	public String returnType;
	// 泛型参数
	public List<String> genericTypes;
	// 参数名
	public String name;
	// 初始值
	public List<Param> params;
	// 变量
	public List<Variable> variables;
	// method域
	public List<String> methodLines;

	public Method(String returnType, List<String> genericTypes, String name, List<Param> params) {
		this.returnType = returnType;
		this.name = name;
		this.params = params;
	}

	public Variable findVariable(String block, String name) {

		for (Variable variable : variables) {
			if (variable.name.equals(name) && block.startsWith(variable.block)) {
				return variable;
			}
		}
		return null;

	}

}

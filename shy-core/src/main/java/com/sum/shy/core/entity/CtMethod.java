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
	// 可能抛出的异常
	public List<String> exceptions;

	/**
	 * 构造
	 * 
	 * @param returnType
	 * @param name
	 * @param params
	 * @param exceptions
	 * @param annotations
	 */
	public CtMethod(Type returnType, String name, List<Param> params, List<String> exceptions,
			List<String> annotations) {
		this.type = returnType;
		this.name = name;
		this.params = params;
		this.exceptions = exceptions;
		this.annotations = annotations != null ? annotations : new ArrayList<>();
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

	public boolean isSame(String methodName, List<Type> parameterTypes) {
		if (name.equals(methodName)) {
			if (params.size() == parameterTypes.size()) {
				int count = 0;
				for (Param param : params) {
					if (!param.type.equals(parameterTypes.get(count++))) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

}

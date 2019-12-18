package com.sum.shy.core.clazz.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.sum.shy.core.clazz.api.AbsElement;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.type.api.Type;

public class CtMethod extends AbsElement {
	// 参数名
	public String name;
	// 初始值
	public List<Param> params;
	// 可能抛出的异常
	public List<String> exceptions;
	// 变量
	public List<Variable> variables = new ArrayList<>();
	// method域
	public List<Line> methodLines;

	/**
	 * 
	 * @param annotations
	 * @param scope
	 * @param returnType
	 * @param isSync
	 * @param name
	 * @param params
	 * @param exceptions
	 */
	public CtMethod(List<String> annotations, String scope, boolean isSync, Type returnType, String name,
			List<Param> params, List<String> exceptions) {
		// 注解
		setAnnotations(annotations);
		// 域
		this.scope = scope;
		// 是否同步
		this.isSync = isSync;
		// 类型
		this.type = returnType;

		this.name = name;
		this.params = params;
		this.exceptions = exceptions;

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

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
	 * 构造
	 * 
	 * @param returnType
	 * @param name
	 * @param params
	 * @param exceptions
	 * @param annotations
	 */
	public CtMethod(String scope, Type returnType, boolean isSync, String name, List<Param> params,
			List<String> exceptions, List<String> annotations) {
		// 注解
		setAnnotations(annotations);
		// 域
		setScope(scope);
		// 是否同步
		setSync(isSync);
		// 类型
		setType(returnType);

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

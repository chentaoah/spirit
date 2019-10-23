package com.sum.shy.clazz;

import java.util.List;

public class Method {
	// 类型
	public String returnType;
	// 参数名
	public String name;
	// 初始值
	public List<Param> params;
	// method域
	public List<String> methodLines;

	public Method(String returnType, String name, List<Param> params) {
		this.returnType = returnType;
		this.name = name;
		this.params = params;
	}

}

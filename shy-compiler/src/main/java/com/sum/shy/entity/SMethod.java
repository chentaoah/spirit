package com.sum.shy.entity;

import java.util.ArrayList;
import java.util.List;

public class SMethod {
	// 类型
	public String returnType;
	// 参数名
	public String name;
	// 初始值
	public List<SVar> params;

	// method域
	public List<String> methodLines = new ArrayList<>();

	public SMethod(String returnType, String name, List<SVar> params) {
		this.returnType = returnType;
		this.name = name;
		this.params = params;
	}

}

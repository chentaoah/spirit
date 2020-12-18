package com.sum.spirit.starter.kit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sum.spirit.starter.kit.common.Result;
import com.sum.spirit.starter.kit.pojo.MethodInfo;

@RequestMapping("/spirit/kit")
public class KitController {

	@PostMapping("/method")
	@SuppressWarnings("unused")
	public Result<List<MethodInfo>> method(@RequestParam Map<String, String> params) {
		String fileName = params.get("fileName");
		String methodName = params.get("methodName");
		String variableName = params.get("variableName");
		String incompleteName = params.get("incompleteName");
		Integer lineNumber = Integer.valueOf(params.get("lineNumber"));
		// 1.进行不完整编译，文档解析只到当前行，且只推导当前类
		// 2.找到对应的类，并找到对应的方法，从指定行，反向查找变量
		return Result.success(new ArrayList<MethodInfo>());
	}

}

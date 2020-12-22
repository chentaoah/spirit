package com.sum.spirit.starter.kit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sum.spirit.starter.kit.common.Result;
import com.sum.spirit.starter.kit.pojo.MethodInfo;

@RequestMapping("/spirit/kit")
public class KitController {

	@PostMapping("/method")
	@SuppressWarnings("unused")
	public Result<List<MethodInfo>> method(@RequestBody Map<String, Object> params) {
		String fileName = (String) params.get("fileName");
		String text = (String) params.get("text");
		Integer lineNumber = Integer.valueOf((String) params.get("lineNumber"));
		// 1.进行不完整编译，文档解析只到当前行，且只推导当前类
		// 2.找到对应的类，并找到对应的方法，从指定行，反向查找变量
		return Result.success(new ArrayList<MethodInfo>());
	}

}

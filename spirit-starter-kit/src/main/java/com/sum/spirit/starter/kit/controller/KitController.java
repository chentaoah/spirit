package com.sum.spirit.starter.kit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.starter.JavaEngine;
import com.sum.spirit.starter.kit.common.Result;
import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.utils.ConfigUtils;

@RequestMapping("/spirit/kit")
public class KitController {

	@Autowired
	public JavaEngine engine;

	@PostMapping("/method")
	@SuppressWarnings("unused")
	public Result<List<MethodInfo>> method(@RequestParam Map<String, Object> params, @RequestBody String text) {
		String fileName = (String) params.get("fileName");
		Integer lineNumber = Integer.valueOf((String) params.get("lineNumber"));

		String inputPath = ConfigUtils.getProperty(Constants.INPUT_ARG_KEY);
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);
		Map<String, Object> arguments = new HashMap<>();
		arguments.put(Constants.INPUT_ARG_KEY, inputPath);
		arguments.put(Constants.FILENAME_EXTENSION_KEY, extension);
		engine.run(arguments);

		// 1.进行不完整编译，文档解析只到当前行，且只推导当前类
		// 2.找到对应的类，并找到对应的方法，从指定行，反向查找变量
		return Result.success(new ArrayList<MethodInfo>());
	}

}

package com.sum.spirit.starter.kit.assistant;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.starter.kit.pojo.Result;

@RestController
@RequestMapping("/spirit/kit")
public class AssistantController {

	@Autowired
	public MethodService service;

	@PostMapping("/getMethodInfos")
	public Result<List<MethodInfo>> getMethodInfos(@RequestBody Map<String, Object> params) {
		String filePath = (String) params.get("filePath");
		String content = (String) params.get("content");
		Integer lineNumber = (Integer) params.get("lineNumber");
		List<MethodInfo> methodInfos = service.getMethodInfos(filePath, content, lineNumber);
		return Result.success(methodInfos);
	}

}

package com.sum.spirit.starter.kit.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.starter.kit.pojo.Result;

@RestController
@RequestMapping("/spirit/kit")
public class KitController {

	@Autowired
	public KitService service;

	@PostMapping("/getMethodInfos")
	public Result<List<MethodInfo>> getMethodInfos(@RequestParam Map<String, Object> params,
			@RequestBody String content) {
		String fileName = (String) params.get("fileName");
		Integer lineNumber = Integer.valueOf((String) params.get("lineNumber"));
		List<MethodInfo> methodInfos = service.getMethodInfos(fileName, content, lineNumber);
		return Result.success(methodInfos);
	}

}

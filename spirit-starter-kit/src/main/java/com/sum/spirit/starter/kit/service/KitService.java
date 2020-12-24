package com.sum.spirit.starter.kit.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.starter.kit.core.CustomCompiler;
import com.sum.spirit.starter.kit.pojo.MethodInfo;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileHelper;

import cn.hutool.core.io.IoUtil;

@Service
public class KitService {

	@Autowired
	public CustomCompiler compiler;

	public List<MethodInfo> getMethodInfos(String className, String content, Integer lineNumber) {
		// 参数
		String inputPath = ConfigUtils.getProperty(Constants.INPUT_ARG_KEY);
		String extension = ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);
		// 删除后面的行，将该行进行补全，然后截断，剩下待推导部分
		content = completeCode(content, lineNumber);
		// 找到对应class,并找到印记，获取推导出的类型，并返回所有该类型的方法信息
		Map<String, InputStream> inputs = FileHelper.getFiles(inputPath, extension);
		inputs.put(className, IoUtil.toStream(content, Constants.DEFAULT_CHARSET));
		IType type = compiler.compileAndGetType(inputs, className, lineNumber);
		System.out.println(type.getClassName());
		return null;
	}

	public String completeCode(String content, Integer lineNumber) {
		return content;
	}

}

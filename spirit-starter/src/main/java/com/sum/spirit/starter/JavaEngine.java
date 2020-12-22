package com.sum.spirit.starter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;
import com.sum.spirit.core.AliasReplacer;
import com.sum.spirit.core.RunningMonitor;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.FileHelper;

@Component
public class JavaEngine {

	@Autowired
	public Compiler compiler;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public AliasReplacer replacer;
	@Autowired
	public RunningMonitor monitor;

	public void run(Map<String, Object> params) {
		monitor.printArgs(params);
		long timestamp = System.currentTimeMillis();
		String inputPath = (String) params.get(Constants.INPUT_ARG_KEY);
		String outputPath = (String) params.get(Constants.OUTPUT_ARG_KEY);
		String extension = (String) params.get(Constants.FILENAME_EXTENSION_KEY);
		Map<String, ? extends InputStream> inputs = getInputStreams(inputPath, extension);
		List<IClass> classes = compile(inputs);
		generateFiles(outputPath, classes);
		monitor.printTotalTime(timestamp);
	}

	public Map<String, ? extends InputStream> getInputStreams(String inputPath, String extension) {
		return FileHelper.getFiles(inputPath, extension);
	}

	public List<IClass> compile(Map<String, ? extends InputStream> inputs) {
		return compiler.compile(inputs);
	}

	public void generateFiles(String outputPath, List<IClass> classes) {
		classes.forEach(clazz -> {
			String code = builder.build(clazz);// 输出目标代码
			code = replacer.replace(clazz, code);
			System.out.println(code);
			if (StringUtils.isNotEmpty(outputPath)) {// 生成文件
				FileHelper.generateFile(outputPath, clazz.getClassName(), code);
			}
		});
	}

}

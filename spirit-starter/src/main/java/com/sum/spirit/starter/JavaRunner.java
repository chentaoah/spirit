package com.sum.spirit.starter;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.PostProcessor;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.ConfigUtils;
import com.sum.spirit.utils.FileUtils;
import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;

@Component
@DependsOn("configUtils")
public class JavaRunner implements ApplicationRunner {

	public static final String INPUT_ARG = "input";
	public static final String OUTPUT_ARG = "output";

	@Autowired
	public Compiler compiler;
	@Autowired
	public CodeBuilder builder;
	@Autowired
	public PostProcessor processor;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		processor.whenApplicationStart(args.getSourceArgs());
		// 输入输出
		String inputPath = args.getOptionValues(INPUT_ARG).get(0);
		String outputPath = args.containsOption(OUTPUT_ARG) ? args.getOptionValues(OUTPUT_ARG).get(0) : null;
		// 文件后缀
		String suffix = ConfigUtils.getProperty(Constants.FILE_SUFFIX_KEY);
		Map<String, File> files = FileUtils.getFiles(inputPath, suffix);
		Map<String, IClass> allClasses = compiler.compile(files);
		// 遍历
		allClasses.forEach((className, clazz) -> {
			// 增强类
			processor.whenClassCompileFinish(clazz);
			// 输出目标代码
			String code = builder.build(clazz);
			// 替换别名
			code = processor.processCode(clazz, code);
			// 生成文件
			if (StringUtils.isNotEmpty(outputPath)) {
				FileUtils.generateFile(outputPath, clazz.getClassName(), code);
			}
		});

		processor.whenApplicationEnd(args.getSourceArgs(), files);
	}

}

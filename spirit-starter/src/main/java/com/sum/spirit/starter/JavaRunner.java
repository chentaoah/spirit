package com.sum.spirit.starter;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.AliasReplacer;
import com.sum.spirit.pojo.clazz.impl.IClass;
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
	public AliasReplacer replacer;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// 是否自动编译
		boolean autoRun = ConfigUtils.getProperty(Constants.AUTO_RUN_KEY, false);
		if (!autoRun) {
			return;
		}

		printArgs(args.getSourceArgs());

		long timestamp = System.currentTimeMillis();
		String inputPath = args.getOptionValues(INPUT_ARG).get(0);
		String outputPath = args.containsOption(OUTPUT_ARG) ? args.getOptionValues(OUTPUT_ARG).get(0) : null;
		String suffix = ConfigUtils.getProperty(Constants.FILE_SUFFIX_KEY, "sp");

		Map<String, File> files = FileUtils.getFiles(inputPath, suffix);
		Map<String, FileInputStream> fileInputs = new HashMap<>();
		files.forEach((path, file) -> fileInputs.put(path, FileUtils.getFileInputStream(file)));

		List<IClass> classes = compiler.compile(fileInputs);
		classes.forEach(clazz -> buildCodeAndGenerateFile(outputPath, clazz));

		printTotalTime(timestamp);
	}

	public void printArgs(String[] sourceArgs) {
		for (String arg : sourceArgs) {
			System.out.println(arg);
		}
		System.out.println("");
	}

	public void buildCodeAndGenerateFile(String outputPath, IClass clazz) {
		String code = builder.build(clazz);// 输出目标代码
		code = replacer.replace(clazz, code);
		System.out.println(code);
		if (StringUtils.isNotEmpty(outputPath)) {// 生成文件
			FileUtils.generateFile(outputPath, clazz.getClassName(), code);
		}
	}

	public void printTotalTime(long timestamp) {
		System.out.println("Total time:" + (System.currentTimeMillis() - timestamp) + "ms");
	}

}

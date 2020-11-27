package com.sum.spirit.starter;

import java.io.File;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.PostProcessor;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.utils.FileUtils;
import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;

@Component
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

		String inputPath = args.getOptionValues(INPUT_ARG).get(0);
		String outputPath = args.containsOption(OUTPUT_ARG) ? args.getOptionValues(OUTPUT_ARG).get(0) : null;

		Map<String, File> files = FileUtils.getFiles(inputPath);
		Map<String, IClass> allClasses = compiler.compile(files);
		allClasses.forEach((className, clazz) -> {

			processor.whenClassCompileFinish(clazz);
			String code = builder.build(clazz);
			code = processor.processCode(clazz, code);

			if (StringUtils.isNotEmpty(outputPath)) {
				FileUtils.generateFile(outputPath, clazz.getClassName(), code);
			}
		});

		processor.whenApplicationEnd(args.getSourceArgs(), files);
	}

}

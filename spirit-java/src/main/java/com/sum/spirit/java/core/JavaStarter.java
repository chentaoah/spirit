package com.sum.spirit.java.core;

import java.io.File;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;
import com.sum.spirit.core.PostProcessor;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.utils.FileUtils;

public class JavaStarter {

	public static final String SCAN_PACKAGE = "com.sum.spirit";
	public static ApplicationContext context = new AnnotationConfigApplicationContext(SCAN_PACKAGE);
	public static Compiler compiler = context.getBean(Compiler.class);
	public static CodeBuilder builder = context.getBean(CodeBuilder.class);
	public static PostProcessor processor = context.getBean(PostProcessor.class);

	public static void main(String[] args) {

		processor.whenApplicationStart(args);

		String inputPath = args[0];
		String outputPath = args.length >= 1 ? args[1] : null;
		Map<String, File> files = FileUtils.getFiles(inputPath);

		Map<String, IClass> allClasses = compiler.compile(files);
		allClasses.forEach((className, clazz) -> {

			processor.whenClassCompileFinish(clazz);
			String code = builder.build(clazz);
			code = processor.processCode(clazz, code);

			if (StringUtils.isNotEmpty(outputPath))
				FileUtils.generateFile(outputPath, clazz.getClassName(), code);
		});

		processor.whenApplicationEnd(args, files);
	}
}

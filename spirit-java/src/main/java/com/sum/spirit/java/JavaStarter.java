package com.sum.spirit.java;

import java.io.File;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.api.Compiler;
import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.lib.StringUtils;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.utils.FileUtils;

public class JavaStarter {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		ApplicationContext context = new AnnotationConfigApplicationContext("com.sum.spirit");
		Compiler compiler = context.getBean(Compiler.class);
		CodeBuilder builder = context.getBean(CodeBuilder.class);
		PostProcessor processor = context.getBean(PostProcessor.class);

		processor.postStartProcessor(args);

		String inputPath = args[0];
		String outputPath = args.length >= 1 ? args[1] : null;

		Map<String, File> files = FileUtils.getFiles(inputPath);
		Map<String, IClass> allClasses = compiler.compile(files);

		allClasses.forEach((className, clazz) -> {

			String code = builder.build(clazz);
			code = processor.postCodeProcessor(args, clazz, code);

			if (StringUtils.isNotEmpty(outputPath))
				FileUtils.generateFile(outputPath, clazz.getClassName(), code);
		});

		processor.postEndProcessor(args, files);
	}
}

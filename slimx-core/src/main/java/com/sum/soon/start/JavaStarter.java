package com.sum.soon.start;

import java.io.File;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.pisces.utils.StringUtils;
import com.sum.soon.api.CodeBuilder;
import com.sum.soon.api.Compiler;
import com.sum.soon.api.PostProcessor;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.utils.FileUtils;

public class JavaStarter {

	public static Compiler compiler = ProxyFactory.get(Compiler.class);
	public static CodeBuilder builder = ProxyFactory.get(CodeBuilder.class);
	public static PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	public static void main(String[] args) {

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

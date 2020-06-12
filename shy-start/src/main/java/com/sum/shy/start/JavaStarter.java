package com.sum.shy.start;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.CodeBuilder;
import com.sum.shy.api.Compiler;
import com.sum.shy.api.PostProcessor;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.utils.FileUtils;

public class JavaStarter {

	public static Compiler compiler = ProxyFactory.get(Compiler.class);

	public static CodeBuilder builder = ProxyFactory.get(CodeBuilder.class);

	public static PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	public static void main(String[] args) {

		processor.postStartProcessor(args);

		String inputPath = args[0];
		String outputPath = args[1];

		Map<String, File> files = new LinkedHashMap<>();
		FileUtils.getFiles(inputPath, "", files);

		Map<String, IClass> allClasses = compiler.compile(files);

		for (IClass clazz : allClasses.values()) {
			String code = builder.build(clazz);
			code = processor.postCodeProcessor(args, clazz, code);
			FileUtils.generateFile(outputPath, clazz.packageStr, clazz.getSimpleName(), code);
		}

		processor.postEndProcessor(args, files);

	}

}

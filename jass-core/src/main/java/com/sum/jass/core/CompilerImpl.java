package com.sum.jass.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.jass.api.ClassResolver;
import com.sum.jass.api.Compiler;
import com.sum.jass.api.DocumentReader;
import com.sum.jass.api.MemberVisiter;
import com.sum.jass.api.PostProcessor;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.common.Context;
import com.sum.jass.pojo.element.Document;
import com.sum.jass.utils.TypeUtils;

public class CompilerImpl implements Compiler {

	public static DocumentReader reader = ProxyFactory.get(DocumentReader.class);

	public static ClassResolver resolver = ProxyFactory.get(ClassResolver.class);

	public static MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);

	public static PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	@Override
	public Map<String, IClass> compile(Map<String, File> files) {

		Map<String, IClass> allClasses = new LinkedHashMap<>();

		files.forEach((path, file) -> {
			// 1.read file
			Document document = reader.read(file);

			// 2.post document processor
			processor.postDocumentProcessor(path, document);

			// 3.resolve classes
			List<IClass> classes = resolver.resolve(TypeUtils.getPackage(path), document);
			classes.forEach((clazz) -> {
				allClasses.put(clazz.getClassName(), clazz);
				// 4.post class processor
				processor.postClassProcessor(clazz.getClassName(), clazz);
			});
		});

		// 5.put in context
		Context.get().classes = allClasses;

		// 6.preprocessor.For example，AutoImporter
		processor.postBeforeProcessor(files, allClasses);

		// 7.perform members derivation
		visiter.visit(allClasses);

		// 8.post processor
		processor.postAfterProcessor(allClasses);

		return allClasses;
	}

}

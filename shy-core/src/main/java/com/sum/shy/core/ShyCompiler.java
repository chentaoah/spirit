package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ClassResolver;
import com.sum.shy.api.DocumentReader;
import com.sum.shy.api.MemberVisiter;
import com.sum.shy.api.PostProcessor;
import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.core.pojo.Context;
import com.sum.shy.core.utils.TypeUtils;
import com.sum.shy.document.pojo.Document;

public class ShyCompiler {

	public DocumentReader reader = ProxyFactory.get(DocumentReader.class);
	public ClassResolver resolver = ProxyFactory.get(ClassResolver.class);
	public MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);
	public PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	public Map<String, IClass> compile(Map<String, File> files) {
		Map<String, IClass> allClasses = new LinkedHashMap<>();
		files.forEach((path, file) -> {
			// 1.read file
			Document document = reader.readDocument(file);
			// 2.post document processor
			processor.postDocumentProcessor(path, document);
			// 3.resolve classes
			String packageStr = TypeUtils.getPackage(path);
			List<IClass> classes = resolver.resolverClasses(packageStr, document);
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
		visiter.visitMembers(allClasses);
		// 8.post processor
		processor.postAfterProcessor(allClasses);

		return allClasses;
	}

}

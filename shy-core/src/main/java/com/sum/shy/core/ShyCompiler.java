package com.sum.shy.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.clazz.api.ClassResolver;
import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.document.api.DocumentReader;
import com.sum.shy.document.pojo.Document;
import com.sum.shy.member.api.MemberVisiter;
import com.sum.shy.pojo.Context;
import com.sum.shy.post.api.PostProcessor;
import com.sum.shy.utils.TypeUtils;

public class ShyCompiler {

	public DocumentReader reader = ProxyFactory.get(DocumentReader.class);
	public ClassResolver resolver = ProxyFactory.get(ClassResolver.class);
	public MemberVisiter visiter = ProxyFactory.get(MemberVisiter.class);
	public PostProcessor processor = ProxyFactory.get(PostProcessor.class);

	public Map<String, IClass> compile(Map<String, File> files) {
		Map<String, IClass> allClasses = new LinkedHashMap<>();
		files.forEach((className, file) -> {
			// 1.read file
			Document document = reader.readDocument(file);
			document.debug();
			// 2.resolve classes
			String packageStr = TypeUtils.getPackage(className);
			List<IClass> classes = resolver.resolverClasses(packageStr, document);
			classes.forEach((clazz) -> allClasses.put(clazz.getClassName(), clazz));
		});
		// 3.put in context
		Context.get().classes = allClasses;
		// 4.preprocessor.For example，AutoImporter
		processor.postBeforeProcessor(allClasses);
		// 5.perform members derivation
		visiter.visitMembers(allClasses);
		// 6.post processor
		processor.postAfterProcessor(allClasses);

		return allClasses;
	}

}

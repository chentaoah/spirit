package com.sum.spirit.core;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassResolver;
import com.sum.spirit.api.Compiler;
import com.sum.spirit.api.DocumentReader;
import com.sum.spirit.api.MemberVisiter;
import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.Context;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.utils.TypeUtils;

@Component
public class CompilerImpl implements Compiler {

	@Autowired
	public DocumentReader reader;
	@Autowired
	public ClassResolver resolver;
	@Autowired
	public MemberVisiter visiter;
	@Autowired
	public PostProcessor processor;

	@Override
	public Map<String, IClass> compile(Map<String, File> files) {

		Map<String, IClass> allClasses = new LinkedHashMap<>();

		files.forEach((path, file) -> {
			// 1.read file
			Document document = reader.readFile(file);

			// 2.post document processor
			processor.whenDocumentReadFinish(path, document);

			// 3.resolve classes
			List<IClass> classes = resolver.resolve(TypeUtils.getPackage(path), document);
			classes.forEach((clazz) -> allClasses.put(clazz.getClassName(), clazz));
		});

		// 5.put in context
		Context.get().classes = allClasses;

		// 6.preprocessor.For example, AutoImporter
		processor.whenAllClassesResolveFinish(files, allClasses);

		// 7.perform members derivation
		visiter.visit(allClasses);

		return allClasses;
	}
}

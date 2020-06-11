package com.sum.shy.api.service;

import java.io.File;
import java.util.Map;

import com.sum.shy.api.PostProcessor;
import com.sum.shy.clazz.IClass;
import com.sum.shy.common.MethodContext;
import com.sum.shy.element.Document;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

public class PostProcessorImpl implements PostProcessor {

	public long timestamp;

	@Override
	public void postStartProcessor(String[] args) {
		System.out.println("input:" + args[0]);
		System.out.println("output:" + args[1]);
		System.out.println("");
		timestamp = System.currentTimeMillis();
	}

	@Override
	public void postDocumentProcessor(String path, Document document) {
		document.debug();
	}

	@Override
	public void postClassProcessor(String className, IClass clazz) {
		// ignore
	}

	@Override
	public void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses) {
		AutoImporter.doImport(files, allClasses);
	}

	@Override
	public void postAfterProcessor(Map<String, IClass> allClasses) {
		// ignore
	}

	@Override
	public void postElementProcessor(Line line, Element element) {
		// ignore
	}

	@Override
	public void postBeforeVisitProcessor(IClass clazz, MethodContext context, Element element) {
		// ignore
	}

	@Override
	public void postAfterVisitProcessor(IClass clazz, MethodContext context, Element element) {
		// ignore
	}

	@Override
	public String postCodeProcessor(String[] args, IClass clazz, String code) {
		code = AliasReplacer.replace(clazz, code);
		System.out.println(code);
		return code;
	}

	@Override
	public void postEndProcessor(String[] args, Map<String, File> files) {
		System.out.println("Total compilation time:" + (System.currentTimeMillis() - timestamp) + "ms");
		timestamp = System.currentTimeMillis();
	}

}

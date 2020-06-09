package com.sum.shy.api.service;

import java.io.File;
import java.util.Map;

import com.sum.shy.api.PostProcessor;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Document;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

public class PostProcessorImpl implements PostProcessor {

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

}

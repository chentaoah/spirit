package com.sum.shy.post;

import java.io.File;
import java.util.Map;

import com.sum.shy.api.PostProcessor;
import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.document.pojo.Document;
import com.sum.shy.post.impl.AutoImporter;

public class DefaultPostProcessor implements PostProcessor {

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

}

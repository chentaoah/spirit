package com.sum.jass.core;

import java.io.File;
import java.util.Map;

import com.sum.jass.api.PostProcessor;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Document;

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
	public void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses) {
		AutoImporter.doImport(files, allClasses);
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

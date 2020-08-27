package com.sum.spirit.core;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.PostProcessor;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Document;

@Component
public class PostProcessorImpl implements PostProcessor {

	@Autowired
	public AliasReplacer replacer;
	@Autowired
	public AutoImporter importer;

	public long timestamp;

	@Override
	public void whenApplicationStart(String[] args) {
		System.out.println("input:" + args[0]);
		System.out.println("output:" + args[1]);
		System.out.println("");
		timestamp = System.currentTimeMillis();
	}

	@Override
	public void whenDocumentReadFinish(String path, Document document) {
		document.debug();
	}

	@Override
	public void preprocessBeforeVisit(Map<String, File> files, Map<String, IClass> allClasses) {
		importer.doImport(files, allClasses);
	}

	@Override
	public void whenClassCompileFinish(IClass clazz) {

	}

	@Override
	public String processCode(IClass clazz, String code) {
		code = replacer.replace(clazz, code);
		System.out.println(code);
		return code;
	}

	@Override
	public void whenApplicationEnd(String[] args, Map<String, File> files) {
		System.out.println("Total compilation time:" + (System.currentTimeMillis() - timestamp) + "ms");
		timestamp = System.currentTimeMillis();
	}

}

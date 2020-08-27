package com.sum.spirit.api;

import java.io.File;
import java.util.Map;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Document;

public interface PostProcessor {

	void whenApplicationStart(String[] args);

	void whenDocumentReadFinish(String path, Document document);

	void preprocessBeforeVisit(Map<String, File> files, Map<String, IClass> allClasses);

	void whenClassCompileFinish(IClass clazz);

	String processCode(IClass clazz, String code);

	void whenApplicationEnd(String[] args, Map<String, File> files);

}

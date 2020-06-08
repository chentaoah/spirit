package com.sum.shy.post.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.document.pojo.Document;

@Service("postProcessor")
public interface PostProcessor {

	void postDocumentProcessor(String path, Document document);

	void postClassProcessor(String className, IClass clazz);

	void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses);

	void postAfterProcessor(Map<String, IClass> allClasses);

}

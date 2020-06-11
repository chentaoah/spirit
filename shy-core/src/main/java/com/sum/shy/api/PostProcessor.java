package com.sum.shy.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.common.MethodContext;
import com.sum.shy.element.Document;
import com.sum.shy.element.Element;
import com.sum.shy.element.Line;

@Service("post_processor")
public interface PostProcessor {

	void postStartProcessor(String[] args);

	void postDocumentProcessor(String path, Document document);

	void postClassProcessor(String className, IClass clazz);

	void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses);

	void postAfterProcessor(Map<String, IClass> allClasses);

	void postElementProcessor(Line line, Element element);

	void postBeforeVisitProcessor(IClass clazz, MethodContext context, Element element);

	void postAfterVisitProcessor(IClass clazz, MethodContext context, Element element);

	String postCodeProcessor(String[] args, IClass clazz, String code);

	void postEndProcessor(String[] args, Map<String, File> files);

}

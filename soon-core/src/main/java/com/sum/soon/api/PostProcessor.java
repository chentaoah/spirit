package com.sum.soon.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.common.MethodContext;
import com.sum.soon.pojo.element.Document;
import com.sum.soon.pojo.element.Element;
import com.sum.soon.pojo.element.Line;

@Service("post_processor")
public interface PostProcessor {

	void postStartProcessor(String[] args);

	void postDocumentProcessor(String path, Document document);

	default void postClassProcessor(String className, IClass clazz) {
		// ignore
	}

	void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses);

	default void postAfterProcessor(Map<String, IClass> allClasses) {
		// ignore
	}

	default void postElementProcessor(Line line, Element element) {
		// ignore
	}

	default void postBeforeVisitProcessor(IClass clazz, MethodContext context, Element element) {
		// ignore
	}

	default void postAfterVisitProcessor(IClass clazz, MethodContext context, Element element) {
		// ignore
	}

	String postCodeProcessor(String[] args, IClass clazz, String code);

	void postEndProcessor(String[] args, Map<String, File> files);

}

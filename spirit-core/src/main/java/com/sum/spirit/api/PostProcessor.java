package com.sum.spirit.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.element.Line;

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

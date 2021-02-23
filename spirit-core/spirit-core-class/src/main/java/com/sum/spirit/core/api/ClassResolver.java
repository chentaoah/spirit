package com.sum.spirit.core.api;

import java.util.Map;

import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.element.entity.Document;

public interface ClassResolver {

	Map<String, IClass> resolveClasses(String packageStr, Document document);

}

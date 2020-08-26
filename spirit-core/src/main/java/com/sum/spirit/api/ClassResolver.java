package com.sum.spirit.api;

import java.util.List;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Document;

public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

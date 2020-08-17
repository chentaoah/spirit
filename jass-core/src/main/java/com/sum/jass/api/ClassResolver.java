package com.sum.jass.api;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

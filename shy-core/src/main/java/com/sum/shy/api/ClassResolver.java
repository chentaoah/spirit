package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

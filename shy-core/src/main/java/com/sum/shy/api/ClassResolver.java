package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Document;

@Service("classResolver")
public interface ClassResolver {

	List<IClass> resolverClasses(String packageStr, Document document);

}

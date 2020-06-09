package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.pojo.IClass;
import com.sum.shy.document.pojo.Document;

@Service("classResolver")
public interface ClassResolver {

	List<IClass> resolverClasses(String packageStr, Document document);

}

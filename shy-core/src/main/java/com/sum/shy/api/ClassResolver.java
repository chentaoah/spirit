package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

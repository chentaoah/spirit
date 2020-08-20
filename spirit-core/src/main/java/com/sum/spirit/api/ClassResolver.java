package com.sum.spirit.api;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

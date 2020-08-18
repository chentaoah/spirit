package com.sum.soon.api;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

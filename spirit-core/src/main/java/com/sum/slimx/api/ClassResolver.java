package com.sum.slimx.api;

import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;
import com.sum.slimx.pojo.element.Document;

@Service("class_resolver")
public interface ClassResolver {

	List<IClass> resolve(String packageStr, Document document);

}

package com.sum.shy.event;

import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.pojo.IClass;

@Service("postProcessor")
public interface PostProcessor {

	void postBeforeProcessor(Map<String, IClass> allClasses);

	void postAfterProcessor(Map<String, IClass> allClasses);

}

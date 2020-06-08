package com.sum.shy.post.api;

import java.io.File;
import java.util.Map;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.pojo.IClass;

@Service("postProcessor")
public interface PostProcessor {

	void postBeforeProcessor(Map<String, File> files, Map<String, IClass> allClasses);

	void postAfterProcessor(Map<String, IClass> allClasses);

}

package com.sum.spirit.api;

import java.io.File;
import java.util.Map;

import com.sum.spirit.pojo.clazz.IClass;

public interface Compiler {

	Map<String, IClass> compile(Map<String, File> files);

	boolean contains(String className);

	IClass findClass(String className);

	String getClassName(String simpleName);

}

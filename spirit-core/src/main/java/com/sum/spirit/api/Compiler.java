package com.sum.spirit.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sum.spirit.pojo.clazz.impl.IClass;

public interface Compiler {

	List<IClass> compile(Map<String, File> files, String... includePaths);

}

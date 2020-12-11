package com.sum.spirit.api;

import java.io.File;
import java.util.Map;

import com.sum.spirit.pojo.clazz.impl.IClass;

public interface Compiler {

	Map<String, IClass> compile(Map<String, File> files);

}

package com.sum.spirit.api;

import java.io.InputStream;
import java.util.Map;

import com.sum.spirit.core.clazz.pojo.IClass;

public interface Compiler {

	Map<String, IClass> compile(String name, InputStream input, String... arguments);

}

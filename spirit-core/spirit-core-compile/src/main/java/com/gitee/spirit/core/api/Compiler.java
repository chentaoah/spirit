package com.gitee.spirit.core.api;

import java.io.InputStream;
import java.util.Map;

import com.gitee.spirit.core.clazz.entity.IClass;

public interface Compiler {

	Map<String, IClass> compile(String name, InputStream input, String... arguments);

}

package com.sum.spirit.api;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.sum.spirit.pojo.clazz.impl.IClass;

public interface Compiler {

	public List<IClass> compile(Map<String, ? extends InputStream> inputs, String... includePaths);

}

package com.sum.spirit.core;

import java.util.Map;

import com.sum.spirit.api.Compiler;
import com.sum.spirit.pojo.clazz.IClass;

public abstract class AbsCompilerImpl implements Compiler {

	// 此次编译的所有的类
	public Map<String, IClass> classes;

	@Override
	public boolean contains(String className) {
		return classes.containsKey(className);
	}

	@Override
	public IClass findClass(String className) {
		return classes.get(className);
	}

	@Override
	public String getClassName(String simpleName) {
		for (String className : classes.keySet()) {
			if (className.endsWith("." + simpleName))
				return className;
		}
		return null;
	}

}

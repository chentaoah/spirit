package com.sum.spirit.core;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.clazz.IClass;

@Component
@Order(-100)
public class SystemClassLoader extends AbsClassLoader {

	// 此次编译的所有的类
	public Map<String, IClass> classes;

	@Override
	public String getClassName(String simpleName) {
		for (String className : classes.keySet()) {
			if (className.endsWith("." + simpleName))
				return className;
		}
		return null;
	}

	@Override
	public boolean isLoaded(String className) {
		return classes.containsKey(className);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getClass(String className) {
		return (T) classes.get(className);
	}

}

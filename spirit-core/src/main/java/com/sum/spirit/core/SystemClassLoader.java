package com.sum.spirit.core;

import java.util.Map;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.pojo.clazz.IClass;

@Component
@Order(-100)
public class SystemClassLoader implements ClassLoader {

	// 此次编译的所有的类
	public Map<String, IClass> classes;

	@Override
	public void load() {
		// ignore
	}

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
	public boolean shouldImport(String className) {
		return true;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getClass(String className) {
		return (T) classes.get(className);
	}

}

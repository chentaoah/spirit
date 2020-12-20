package com.sum.spirit.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.sum.spirit.pojo.clazz.impl.IClass;

import cn.hutool.core.lang.Assert;

@Component
@Order(-100)
public class CodeClassLoader extends AbsClassLoader {

	public Map<String, IClass> classes = new LinkedHashMap<>();

	@Override
	public String findClassName(String simpleName) {
		for (String className : classes.keySet()) {
			if (className.endsWith("." + simpleName)) {
				return className;
			}
		}
		return null;
	}

	@Override
	public boolean contains(String className) {
		return classes.containsKey(className);
	}

	@Override
	public boolean isloaded(String className) {
		return classes.get(className) != null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getClass(String className) {
		IClass clazz = classes.get(className);
		Assert.notNull(clazz, "Class can not be null!");
		return (T) clazz;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getClasses() {
		return (List<T>) classes.values().stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

}

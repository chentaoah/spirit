package com.gitee.spirit.output.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.clazz.AbstractImportSelector;
import com.gitee.spirit.core.clazz.utils.TypeUtils;

@Component
@Order(-80)
public class ExtImportSelector extends AbstractImportSelector {

	@Autowired
	public ExtClassLoader loader;

	@Override
	public boolean canHandle(String className) {
		return loader.contains(className);
	}

	@Override
	public String findClassName(String simpleName) {
		String targetName = TypeUtils.getTargetName(simpleName);
		boolean isArray = TypeUtils.isArray(simpleName);
		Class<?> clazz = loader.loadClass("java.lang." + targetName);
		if (clazz != null) {
			return isArray ? "[L" + clazz.getName() + ";" : clazz.getName();
		}
		return null;
	}

	@Override
	public boolean shouldImport(String selfClassName, String className) {
		return super.shouldImport(selfClassName, className) && !className.startsWith("java.lang.");
	}

}

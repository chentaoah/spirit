package com.gitee.spirit.output.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.clazz.AbstractImportSelector;
import com.gitee.spirit.core.clazz.utils.TypeUtils;
import com.gitee.spirit.output.java.utils.ReflectUtils;

@Component
public class ExtImportSelector extends AbstractImportSelector {

	@Autowired
	public ExtClassLoader loader;

	@Override
	public boolean isHandle(String className) {
		return loader.contains(className);
	}

	@Override
	public String findClassName(String simpleName) {
		return ReflectUtils.getClassName(TypeUtils.getTargetName(simpleName), TypeUtils.isArray(simpleName));
	}

	@Override
	public boolean shouldImport(String selfClassName, String className) {
		return false;
	}

}

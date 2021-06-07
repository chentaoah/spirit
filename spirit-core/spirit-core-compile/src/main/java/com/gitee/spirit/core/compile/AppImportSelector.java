package com.gitee.spirit.core.compile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.clazz.AbstractImportSelector;

@Component
@Order(-100)
public class AppImportSelector extends AbstractImportSelector {

	@Autowired
	public AppClassLoader loader;

	@Override
	public boolean canHandle(String className) {
		return loader.contains(className);
	}

	@Override
	public String findClassName(String simpleName) {
		return ListUtils.findOne(loader.getNames(), className -> className.endsWith("." + simpleName));
	}

}

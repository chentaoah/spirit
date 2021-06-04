package com.gitee.spirit.core.clazz;

import com.gitee.spirit.core.api.ImportSelector;
import com.gitee.spirit.core.clazz.utils.TypeUtils;

public abstract class AbstractImportSelector implements ImportSelector {

	@Override
	public boolean shouldImport(String selfClassName, String className) {
		if (selfClassName.equals(className)) {
			return false;
		}
		if (TypeUtils.isSamePackage(selfClassName, className)) {
			return false;
		}
		return true;
	}

}

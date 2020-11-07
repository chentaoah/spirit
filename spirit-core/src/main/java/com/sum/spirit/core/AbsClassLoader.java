package com.sum.spirit.core;

import com.sum.spirit.api.ClassLoader;
import com.sum.spirit.utils.TypeUtils;

public abstract class AbsClassLoader implements ClassLoader {

	@Override
	public void load() {
		// ignore
	}

	@Override
	public boolean shouldImport(String selfClassName, String className) {
		// 类名相同不用添加
		if (selfClassName.equals(className))
			return false;
		// 同个包下不用添加
		if (TypeUtils.isSamePackage(selfClassName, className))
			return false;

		return true;
	}

	@Override
	public <T> T getClass(String className) {
		return null;
	}

}

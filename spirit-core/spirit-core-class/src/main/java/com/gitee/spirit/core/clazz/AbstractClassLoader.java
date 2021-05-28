package com.gitee.spirit.core.clazz;

import java.net.URL;

import com.gitee.spirit.core.api.ClassLoader;
import com.gitee.spirit.core.api.ImportSelector;
import com.gitee.spirit.core.clazz.utils.TypeUtils;

public abstract class AbstractClassLoader<T> implements ClassLoader<T>, ImportSelector {

	@Override
	public T loadClass(String name) {
		URL resource = findResource(name);
		return defineClass(name, resource);
	}

	@Override
	public boolean isHandle(String className) {
		return contains(className);
	}

	@Override
	public boolean shouldImport(String selfName, String className) {
		if (selfName.equals(className)) {
			return false;
		}
		if (TypeUtils.isSamePackage(selfName, className)) {
			return false;
		}
		return true;
	}

	public abstract URL findResource(String name);

	public abstract T defineClass(String name, URL resource);

}

package com.sum.spirit.core.compile;

import java.net.URL;

import com.sum.spirit.core.api.ClassLoader;
import com.sum.spirit.core.api.ImportSelector;
import com.sum.spirit.core.clazz.utils.TypeUtils;

public abstract class AbstractClassLoader<T> implements ClassLoader<T>, ImportSelector {

	@Override
	public T loadClass(String name) {
		URL resource = findResource(name);
		return defineClass(name, resource);
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

package com.gitee.spirit.core.clazz;

import java.net.URL;

import com.gitee.spirit.core.api.ClassLoader;

public abstract class AbstractClassLoader<T> implements ClassLoader<T> {

	@Override
	public T loadClass(String name) {
		URL resource = getResource(name);
		return defineClass(name, resource);
	}

	public abstract URL getResource(String name);

	public abstract T defineClass(String name, URL resource);

}

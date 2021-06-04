package com.gitee.spirit.core.api;

import java.net.URL;
import java.util.List;

public interface ClassLoader<T> {

	List<URL> getResources(String name);

	List<String> getNames();

	boolean contains(String name);

	T loadClass(String name);

	List<T> getAllClasses();

}

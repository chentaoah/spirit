package com.gitee.spirit.core.api;

import java.net.URL;
import java.util.List;

public interface ClassLoader<T> {

	URL getResource(String name);

	List<String> getNames();

	boolean contains(String name);

	T loadClass(String name);

	List<T> getAllClasses();

}

package com.sum.spirit.api;

import java.net.URL;
import java.util.List;

public interface ClassLoader<T> {

	List<URL> getResources();

	List<String> getNames();

	boolean contains(String name);

	T loadClass(String name);

	T findClass(String name);

	T findLoadedClass(String name);

	List<T> getAllClasses();

}

package com.sum.spirit.api;

import java.util.List;

public interface ClassLoader {

	void prepareEnv();

	String findClassName(String simpleName);

	boolean contains(String className);

	boolean isloaded(String className);

	boolean shouldImport(String selfClassName, String className);

	<T> T getClass(String className);

	<T> List<T> getClasses();

}

package com.sum.spirit.api;

public interface ClassLoader {

	void prepareEnvironment();

	String getClassName(String simpleName);

	boolean isLoaded(String className);

	boolean shouldImport(String selfClassName, String className);

	<T> T getClass(String className);

}

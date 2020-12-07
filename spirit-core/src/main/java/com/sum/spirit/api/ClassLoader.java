package com.sum.spirit.api;

public interface ClassLoader {

	void prepare();

	String getClassName(String simpleName);

	boolean isLoaded(String className);

	boolean shouldImport(String selfClassName, String className);

	<T> T getClass(String className);

}

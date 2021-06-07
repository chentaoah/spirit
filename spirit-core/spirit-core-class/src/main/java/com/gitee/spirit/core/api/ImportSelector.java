package com.gitee.spirit.core.api;

public interface ImportSelector {

	boolean canHandle(String className);

	String findClassName(String simpleName);

	boolean shouldImport(String selfClassName, String className);

}

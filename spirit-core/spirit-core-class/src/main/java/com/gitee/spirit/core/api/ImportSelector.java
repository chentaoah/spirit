package com.gitee.spirit.core.api;

public interface ImportSelector {

	boolean isHandle(String className);

	String findClassName(String simpleName);

	boolean shouldImport(String selfClassName, String className);

}

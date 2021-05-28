package com.gitee.spirit.core.api;

public interface ImportSelector {

	String findClassName(String simpleName);

	boolean isHandle(String className);

	boolean shouldImport(String selfName, String className);

}

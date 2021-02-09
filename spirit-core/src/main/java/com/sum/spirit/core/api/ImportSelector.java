package com.sum.spirit.core.api;

public interface ImportSelector {

	String findClassName(String simpleName);

	boolean shouldImport(String selfName, String className);

}

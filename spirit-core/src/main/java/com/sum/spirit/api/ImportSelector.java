package com.sum.spirit.api;

public interface ImportSelector {

	String findClassName(String simpleName);

	boolean shouldImport(String selfName, String className);

}

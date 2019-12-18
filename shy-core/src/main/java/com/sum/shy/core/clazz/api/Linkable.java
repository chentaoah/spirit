package com.sum.shy.core.clazz.api;

/**
 * 可链接的
 * 
 * @author chentao26275
 *
 */
public interface Linkable {

	String getPackage();

	boolean existImport(String typeName);

	String findImport(String simpleName);

	boolean addImport(String className);

}

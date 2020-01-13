package com.sum.shy.clazz;

/**
 * 内部类对象
 * 
 * @author chentao26275
 *
 */
public class InnerClass extends IClass {

	// 内部类指向主类的引用
	public IClass mainClass;

	public String getPackage() {
		return mainClass.getPackage();
	}

	public String getClassName() {
		return mainClass.getClassName() + "." + typeName;
	}

	public boolean existImport(String typeName) {
		return mainClass.existImport(typeName);
	}

	public String findImport(String simpleName) {
		return mainClass.findImport(simpleName);
	}

	public boolean addImport(String className) {
		return mainClass.addImport(className);
	}

}

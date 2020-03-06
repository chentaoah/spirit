package com.sum.shy.core.clazz;

public class CoopClass extends IClass {

	public IClass mainClass;

	public CoopClass(IClass mainClass) {
		this.mainClass = mainClass;
	}

	@Override
	public String findImport(String simpleName) {
		return mainClass.findImport(simpleName);
	}

	@Override
	public boolean addImport(String className) {
		return mainClass.addImport(className);
	}

	@Override
	public String getClassName() {
		return mainClass.getClassName() + getTypeName();
	}

}

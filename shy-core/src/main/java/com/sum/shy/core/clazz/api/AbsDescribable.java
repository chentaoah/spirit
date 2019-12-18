package com.sum.shy.core.clazz.api;

/**
 * 可描述的
 * 
 * @author chentao26275
 *
 */
public abstract class AbsDescribable extends AbsAnnotated {

	public String category;

	public String scope;

	public boolean isSync;

	public boolean isInterface() {
		return "interface".equals(category);
	}

	public boolean isAbstract() {
		return "abstract".equals(category);
	}

	public boolean isClass() {
		return "class".equals(category);
	}

}

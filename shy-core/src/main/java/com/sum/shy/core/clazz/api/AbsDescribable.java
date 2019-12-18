package com.sum.shy.core.clazz.api;

/**
 * 可描述的
 * 
 * @author chentao26275
 *
 */
public abstract class AbsDescribable extends AbsAnnotated {

	public String scope;

	public String category;

	public boolean isSync;

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isSync() {
		return isSync;
	}

	public void setSync(boolean isSync) {
		this.isSync = isSync;
	}

}

package com.sum.shy.core.api;

public interface Type {
	/**
	 * 是否最终的类型
	 * 
	 * @return
	 */
	public boolean isAccurate();

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	public String getTypeName();

}

package com.sum.shy.core.api;

import java.util.List;

public interface Type {

	/**
	 * 是否最终的类型
	 * 
	 * @return
	 */
	public boolean isFinalResult();

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	public String getTypeName();

	/**
	 * 获取泛型
	 * 
	 * @return
	 */
	public List<String> getGenericTypes();

}

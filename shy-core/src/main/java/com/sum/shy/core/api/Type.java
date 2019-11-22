package com.sum.shy.core.api;

import java.util.List;

public interface Type {

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

package com.sum.shy.core.api;

import java.util.List;

public interface Type {

	/**
	 * 获取全名
	 * 
	 * @return
	 */
	public String getClassName();

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
	public List<Type> getGenericTypes();

	/**
	 * 是否数组
	 * 
	 * @return
	 */
	public boolean isArray();

	/**
	 * 是否数组
	 * 
	 * @return
	 */
	public boolean isStr();

}

package com.sum.shy.core.api;

import java.util.List;

public interface Type {

	/**
	 * 获取全名
	 * 
	 * @return
	 */
	String getClassName();

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	String getSimpleName();

	/**
	 * 获取名称
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 获取泛型
	 * 
	 * @return
	 */
	List<Type> getGenericTypes();

	/**
	 * 是否泛型
	 * 
	 * @return
	 */
	boolean isGenericType();

	/**
	 * 是否数组
	 * 
	 * @return
	 */
	boolean isArray();

	/**
	 * 是否数组
	 * 
	 * @return
	 */
	boolean isStr();

	/**
	 * 是否集合
	 * 
	 * @return
	 */
	boolean isList();

	/**
	 * 是否键值对
	 * 
	 * @return
	 */
	boolean isMap();

}

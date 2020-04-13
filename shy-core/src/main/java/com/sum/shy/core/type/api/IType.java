package com.sum.shy.core.type.api;

import java.util.List;

public interface IType {

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
	String getTypeName();

	/**
	 * 是否基本类型
	 * 
	 * @return
	 */
	boolean isPrimitive();

	/**
	 * 是否数组
	 * 
	 * @return
	 */
	boolean isArray();

	/**
	 * 是否泛型
	 * 
	 * @return
	 */
	boolean isGenericType();

	/**
	 * 获取泛型
	 * 
	 * @return
	 */
	List<IType> getGenericTypes();

	/**
	 * 判断是否是一个类的父类
	 * 
	 * @param returnType
	 * @return
	 */
	boolean isAssignableFrom(IType type);

	/**
	 * 是否未知数
	 * 
	 * @return
	 */
	boolean isWildcard();

	/**
	 * 是否没有类型
	 * 
	 * @return
	 */
	boolean isVoid();

	/**
	 * 是否对象
	 * 
	 * @return
	 */
	boolean isObj();

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

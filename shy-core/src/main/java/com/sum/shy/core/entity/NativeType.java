package com.sum.shy.core.entity;

import java.util.Map;

/**
 * 本地原生类型
 *
 * @description：
 * 
 * @version: 1.0
 * @author: chentao26275
 * @date: 2019年11月8日
 */
public class NativeType {

	public Class<?> clazz;// 类名

	public Map<String, Class<?>> genericTypes;// List<E> E-String

	public NativeType(Class<?> clazz, Map<String, Class<?>> genericTypes) {
		this.clazz = clazz;
		this.genericTypes = genericTypes;
	}

}

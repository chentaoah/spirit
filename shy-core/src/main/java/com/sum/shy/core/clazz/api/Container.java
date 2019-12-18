package com.sum.shy.core.clazz.api;

import com.sum.shy.core.clazz.impl.CtField;

/**
 * Element容器
 * 
 * @author chentao26275
 *
 */
public interface Container {

	void addStaticField(CtField field);

	void addField(CtField field);

}

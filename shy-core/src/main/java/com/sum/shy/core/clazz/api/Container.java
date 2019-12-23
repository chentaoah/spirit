package com.sum.shy.core.clazz.api;

import java.util.List;

import com.sum.shy.core.clazz.impl.CtField;
import com.sum.shy.core.clazz.impl.CtMethod;
import com.sum.shy.core.type.api.Type;

/**
 * Element容器
 * 
 * @author chentao26275
 *
 */
public interface Container {

	void addField(CtField field);

	void addMethod(CtMethod method);

	boolean existField(String fieldName);

	boolean existMethod(String methodName, List<Type> parameterTypes);

	CtField findField(String fieldName);

	CtMethod findMethod(String methodName, List<Type> parameterTypes);

	List<Member> getAllElement();

}

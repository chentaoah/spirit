package com.sum.shy.clazz.api;

import java.util.List;

import com.sum.shy.clazz.CtField;
import com.sum.shy.clazz.CtMethod;
import com.sum.shy.type.api.Type;

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

	List<Member> getAllMember();

}

package com.sum.shy.clazz.api;

import java.util.List;

import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.type.api.Type;

/**
 * Element容器
 * 
 * @author chentao26275
 *
 */
public interface Container {

	void addField(IField field);

	void addMethod(IMethod method);

	boolean existField(String fieldName);

	boolean existMethod(String methodName, List<Type> parameterTypes);

	IField findField(String fieldName);

	IMethod findMethod(String methodName, List<Type> parameterTypes);

	List<Member> getAllMember();

}

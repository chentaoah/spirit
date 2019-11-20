package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Token;

public class InvokeVisiter {
	/**
	 * 遍历所有的字段和方法,推导出最终的类型
	 * 
	 * @param classes
	 */
	public static void visit(Map<String, Clazz> classes) {
		for (Clazz clazz : classes.values()) {
			for (Element element : clazz.getAllElement()) {
				CodeType codeType = (CodeType) element.getType();
				Token token = codeType.token;
				if (!token.isType()) {// 如果不是type token,则需要进行推导
					CodeType returnType = visit(clazz, codeType);
					element.setType(returnType);
				}
			}
		}
	}

	public static CodeType visit(Clazz clazz, CodeType type) {

		// 向上获取所有相关的codeType
		List<CodeType> codeTypes = getRelevantType(type);
		// type token
		CodeType returnType = null;
		for (CodeType codeType : codeTypes) {
			Token token = codeType.token;
			if (token.isType()) {// 如果是类型声明
				returnType = codeType;

			} else if (token.isInvokeMember()) {
				returnType = getReturnType(clazz, returnType, token.getPropertiesAtt(), token.getMethodNameAtt());

			} else if (token.isMemberVar()) {
				returnType = getReturnType(clazz, returnType, token.getPropertiesAtt(), null);

			}

		}

		return returnType;
	}

	private static List<CodeType> getRelevantType(CodeType codeType) {
		List<CodeType> codeTypes = new ArrayList<>();
		codeTypes.add(codeType);
		while (true) {
			Token token = codeType.token;
			CodeType lastCodeType = (CodeType) token.getTypeAtt();
			if (lastCodeType != null) {
				codeTypes.add(0, lastCodeType);
				codeType = lastCodeType;
			} else {
				break;
			}
		}
		return codeTypes;
	}

	private static CodeType getReturnType(Clazz clazz, CodeType codeType, List<String> properties, String methodName) {
		Token token = codeType.token;
		if (token.isType()) {
			String typeName = token.getTypeNameAtt();
			String className = clazz.findImport(typeName);
			if (clazz.isFriends(typeName)) {// 如果是友元，则字面意思进行推导
				CodeType returnType = null;
				for (String property : properties) {
					returnType = getFieldType(clazz, properties);
				}
				Clazz returnClazz=clazz.findImport(returnType);
				return getMethodType(anotherClazz, methodName);

			} else if (clazz.isNative(typeName)) {// 如果是本地类型，则通过反射进行推导

			}
		}
		return null;
	}

	private static CodeType getFieldType(Clazz clazz, List<String> properties) {

		return null;
	}

	private static CodeType getMethodType(Clazz clazz, String methodName) {
		// TODO Auto-generated method stub
		return null;
	}

}

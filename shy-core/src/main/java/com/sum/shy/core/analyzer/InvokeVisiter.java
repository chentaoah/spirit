package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Token;

public class InvokeVisiter {

	/**
	 * 遍历所有的字段和方法,推导出最终的类型
	 * 
	 * @param classes
	 */
	public static void visit(Map<String, CtClass> classes) {
		for (CtClass clazz : classes.values()) {
			for (Element element : clazz.getAllElement()) {
				element.setType(visitElement(clazz, element));
			}
		}
	}

	/**
	 * 如果字段类型还没有推导出来，则进行深度推导
	 * 
	 * @param clazz
	 * @param element
	 * @return
	 */
	private static CodeType visitElement(CtClass clazz, Element element) {
		CodeType codeType = (CodeType) element.getType();
		if (!codeType.isFinal()) {// 如果不是type token,则需要进行推导
			return visitCodeType(clazz, codeType);
		}
		return codeType;
	}

	public static CodeType visitCodeType(CtClass clazz, CodeType codeType) {

		// 向上获取所有相关的codeType
		List<CodeType> codeTypes = getRelevantType(codeType);
		// type token
		CodeType returnType = null;
		for (CodeType codeType1 : codeTypes) {
			Token token = codeType1.token;
			if (token.isType()) {// 如果是类型声明
				returnType = codeType1;

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

	private static CodeType getReturnType(CtClass clazz, CodeType codeType, List<String> properties,
			String methodName) {
		if (codeType.isFinal()) {
			String typeName = codeType.token.getTypeNameAtt();
			String className = clazz.findImport(typeName);
			if (Context.get().isFriend(className)) {// 如果是友元，则字面意思进行推导
				CtClass clazz1 = Context.get().findClass(className);
				if (properties.size() > 0) {
					String property = properties.remove(0);// 获取第一个属性
					CtField field = clazz1.findField(property);
					CodeType returnType = visitElement(clazz1, field);// 可能字段类型还需要进行深度推导
					returnType = getReturnType(clazz1, returnType, properties, methodName);
					return returnType;

				} else if (methodName != null) {
					CtMethod method = clazz1.findMethod(methodName);
					return visitElement(clazz1, method);// 可能字段类型还需要进行深度推导
				}

			} else {// 如果是本地类型，则通过反射进行推导

			}
		}
		return null;
	}

}

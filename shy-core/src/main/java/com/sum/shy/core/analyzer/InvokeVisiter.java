package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Token;

public class InvokeVisiter {

	public static CodeType visit(Clazz clazz, CodeType type) {

		// 向上获取所有相关的codeType
		List<CodeType> codeTypes = getRelevantType(type);
		// type token
		CodeType returnType = null;
		// 开始遍历
		for (CodeType codeType : codeTypes) {
			Token token = codeType.token;
			if (token.isType()) {// 如果是类型声明
				returnType = codeType;

			} else if (token.isInvokeInit()) {// 如果是构造方法
				returnType = new CodeType(token.getTypeNameAtt());

			} else if (token.isInvokeMember()) {
				returnType = getReturnType(clazz, returnType, token.getPropertiesAtt(), token.getMethodNameAtt());

			} else if (token.isMemberVar()) {
				returnType = getReturnType(clazz, returnType, token.getPropertiesAtt(), null);

			} else if (token.isValue()) {
				returnType = codeType;

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

	private static CodeType getReturnType(Clazz clazz, CodeType returnType, List<String> properties,
			String methodName) {

		return null;
	}

}

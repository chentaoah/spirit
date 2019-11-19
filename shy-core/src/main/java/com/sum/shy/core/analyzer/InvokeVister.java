package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Token;

public class InvokeVister {

	public static CodeType getType(Clazz clazz, CodeType codeType) {
		// 无论如何先收集一下所有相关的type
		List<CodeType> codeTypes = new ArrayList<>();
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
		//
		// 开始遍历
		String fullName = null;
		for (CodeType type : codeTypes) {
			Token token = type.token;
			if (token.isInvokeInit()) {
				String className = token.getMethodNameAtt();
				// 获取类全名
				fullName = clazz.findImport(className);

			} else if (token.isInvokeMember()) {
				// 根据类名去查找方法
				fullName = getTraceType(fullName, token.getPropertiesAtt(), token.getMethodNameAtt());
			}
		}

		return null;

	}

	private static String getTraceType(String fullName, List<String> propertiesAtt, String methodNameAtt) {
		// 1.尝试从友元中推导
		if (Context.get().isFriends(fullName)) {
			Clazz clazz = Context.get().findClass(fullName);
			for (String property : propertiesAtt) {
				Field field = clazz.findField(property);
			}
		}

		return null;
	}

}

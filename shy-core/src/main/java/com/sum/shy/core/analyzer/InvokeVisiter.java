package com.sum.shy.core.analyzer;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Token;

public class InvokeVisiter {

	public static String visit(Clazz clazz, CodeType codeType) {
		List<CodeType> list = new ArrayList<>();
		while (true) {
			Token token = codeType.token;
			CodeType lastCodeType = (CodeType) token.getTypeAtt();
			if (lastCodeType != null) {
				list.add(lastCodeType);
				codeType = lastCodeType;
			} else {
				break;
			}
		}
		String returnType = null;
		for (CodeType type : list) {
			Token token = codeType.token;
			if (token.isInvokeInit()) {
				returnType = clazz.findImport(token.getMethodNameAtt());

			} else if (token.isInvokeMember()) {
				// 查找class
				Clazz clazz1 = Context.get().findClass(returnType);
				// 查找方法
				Method method = clazz1.findMethod(token.getMethodNameAtt());
				// 获取方法的返回值
				returnType = visit(clazz1, (CodeType) method.returnType);

			} else if (token.isMemberVar()) {
				// 查找class
				Clazz clazz1 = Context.get().findClass(returnType);
				// 查找方法
				Field field = clazz1.findField(token.getPropertiesAtt().get(0));
				// 获取方法的返回值
				returnType = visit(clazz1, (CodeType) field.type);

			} else if (token.isValue()) {
				returnType = (String) token.value;
			}

		}

		return returnType;
	}

}

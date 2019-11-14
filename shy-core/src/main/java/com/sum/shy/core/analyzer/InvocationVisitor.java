package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.JavaBuilder;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.utils.InvokeUtils;

public class InvocationVisitor {

	public static void check(Clazz clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvoke()) {
				if (token.isInvokeInit()) {// 构造方法
					String type = token.getMethodNameAtt();
					token.setReturnTypeAtt(new CodeType(type));// 直接生成codeType,不再通过反射获取类型

				} else if (token.isInvokeStatic()) {// 静态调用
					String type = token.getClassNameAtt();
					Type codeType = new CodeType(type);
					String methodName = token.getMethodNameAtt();
					Type returnType = InvokeUtils.getReturnType(clazz, codeType, methodName);
					token.setReturnTypeAtt(returnType);

				} else if (token.isInvokeMember()) {// 成员方法调用
					Type codeType = token.getTypeAtt();
					List<String> varNames = token.getVarNamesAtt();
					String methodName = token.getMethodNameAtt();
					Type returnType = InvokeUtils.getReturnType(clazz, codeType, varNames, methodName);
					token.setReturnTypeAtt(returnType);

				} else if (token.isInvokeFluent()) {// 流式方法调用
					Token lastToken = stmt.getToken(i - 1);
					Type codeType = lastToken.getReturnTypeAtt();
					List<String> varNames = token.getVarNamesAtt();
					String methodName = token.getMethodNameAtt();
					Type returnType = InvokeUtils.getReturnType(clazz, codeType, varNames, methodName);
					token.setReturnTypeAtt(returnType);

				} else if (token.isInvokeLocal()) {// 本地方法调用
					String methodName = token.getMethodNameAtt();
					Method method = clazz.findMethod(methodName);
					JavaBuilder.convertMethod(new StringBuilder(), clazz, method);// 尝试解析一下
					token.setReturnTypeAtt(method.returnType);
				}

			} else if (token.isStaticVar()) {// 静态变量
				String type = token.getClassNameAtt();
				Type codeType = new CodeType(type);
				List<String> varNames = token.getVarNamesAtt();
				Type returnType = InvokeUtils.getFieldType(clazz, codeType, varNames);
				token.setReturnTypeAtt(returnType);

			} else if (token.isMemberVar()) {// 成员变量
				Type codeType = token.getTypeAtt();
				List<String> varNames = token.getVarNamesAtt();
				Type returnType = InvokeUtils.getFieldType(clazz, codeType, varNames);
				token.setReturnTypeAtt(returnType);

			} else if (token.isMemberVarFluent()) {// 流式成员变量
				Token lastToken = stmt.getToken(i - 1);
				Type codeType = lastToken.getReturnTypeAtt();
				List<String> varNames = token.getVarNamesAtt();
				Type returnType = InvokeUtils.getFieldType(clazz, codeType, varNames);
				token.setReturnTypeAtt(returnType);

			}

		}

	}

}

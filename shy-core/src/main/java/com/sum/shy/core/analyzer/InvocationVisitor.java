package com.sum.shy.core.analyzer;

import java.util.List;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.NativeType;
import com.sum.shy.core.utils.ReflectUtils;

public class InvocationVisitor {

	public static void check(Clazz clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvoke()) {
				if (token.isInvokeInit()) {// 构造方法
					String simpleName = token.getInitMethodNameAtt();
					String className = clazz.findImport(simpleName);
					NativeType returnType = ReflectUtils.getReturnType(className);
					token.setReturnNativeTypeAtt(returnType);

				} else if (token.isInvokeStatic()) {// 静态调用
					String simpleName = token.getClassNameAtt();
					String className = clazz.findImport(simpleName);
					String methodName = token.getStaticMethodNameAtt();
					// 暂不支持方法重载
					NativeType returnType = ReflectUtils.getReturnType(className, methodName);
					token.setReturnNativeTypeAtt(returnType);

				} else if (token.isInvokeMember()) {// 成员方法调用
					NativeType nativeType = token.getNativeTypeAtt();
					List<String> memberVarNames = token.getMemberVarNamesAtt();
					String methodName = token.getMemberMethodNameAtt();
					// 暂不支持方法重载
					NativeType returnType = ReflectUtils.getReturnType(nativeType, memberVarNames, methodName);
					token.setReturnNativeTypeAtt(returnType);

				} else if (token.isInvokeFluent()) {// 流式方法调用
					Token lastToken = stmt.getToken(i - 1);
					NativeType nativeType = lastToken.getReturnNativeTypeAtt();
					List<String> memberVarNames = token.getMemberVarNamesAtt();
					String methodName = token.getMemberMethodNameAtt();
					// 暂不支持方法重载
					NativeType returnType = ReflectUtils.getReturnType(nativeType, memberVarNames, methodName);
					token.setReturnNativeTypeAtt(returnType);

				}

			} else if (token.isMemberVar()) {
				NativeType nativeType = token.getNativeTypeAtt();
				List<String> memberVarNames = token.getMemberVarNamesAtt();
				NativeType returnType = ReflectUtils.getFieldType(nativeType, memberVarNames);
				token.setReturnNativeTypeAtt(returnType);

			} else if (token.isMemberVarFluent()) {// 流式成员变量
				Token lastToken = stmt.getToken(i - 1);
				NativeType nativeType = lastToken.getReturnNativeTypeAtt();
				List<String> memberVarNames = token.getMemberVarNamesAtt();
				NativeType returnType = ReflectUtils.getFieldType(nativeType, memberVarNames);
				token.setReturnNativeTypeAtt(returnType);
			}

		}

	}

}

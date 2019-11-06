package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Type;
import com.sum.shy.core.utils.ReflectUtils;

public class InvokeResolver {

	public static void check(Clazz clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvoke()) {

				if (token.isInvokeStatic()) {// 静态调用
					String simpleName = token.getClassNameAtt();
					String clazzName = clazz.findImport(simpleName);
					String methodName = token.getStaticMethodNameAtt();
					// 暂不支持方法重载
					Type returnType = ReflectUtils.getReturnType(clazzName, methodName);
					token.setReturnTypeAtt(returnType);

				} else if (token.isInvokeMember()) {// 成员方法调用
					String simpleName = token.getTypeAtt().type;
					String clazzName = clazz.findImport(simpleName);
					String methodName = token.getMemberMethodNameAtt();
					// 暂不支持方法重载
					Type returnType = ReflectUtils.getReturnType(token.getTypeAtt(), clazzName, methodName);
					token.setReturnTypeAtt(returnType);
				}
			}
		}
	}

}

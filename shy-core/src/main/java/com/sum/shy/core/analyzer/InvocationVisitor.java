package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Type;
import com.sum.shy.core.utils.ReflectUtils;

public class InvocationVisitor {

	public static void check(Clazz clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvoke()) {
				if (token.isInvokeStatic()) {// 静态调用
					String simpleName = token.getClassNameAtt();
					String className = clazz.findImport(simpleName);
					String methodName = token.getStaticMethodNameAtt();
					// 暂不支持方法重载
					Type returnType = ReflectUtils.getReturnType(className, methodName);
					token.setReturnTypeAtt(returnType);
				} else if (token.isInvokeMember()) {// 成员方法调用
					String simpleName = token.getTypeAtt().name;
					if (Constants.ARRAY_TYPE.equals(simpleName)) {
						String methodName = token.getMemberMethodNameAtt();
						if ("get".equals(methodName))
							token.setReturnTypeAtt(token.getTypeAtt().genericTypes.get(0));

					} else if (Constants.MAP_TYPE.equals(simpleName)) {
						String methodName = token.getMemberMethodNameAtt();
						if ("get".equals(methodName))
							token.setReturnTypeAtt(token.getTypeAtt().genericTypes.get(1));

					} else {
						try {
							String className = clazz.findImport(simpleName);
							String methodName = token.getMemberMethodNameAtt();
							// 暂不支持方法重载
							Type returnType = ReflectUtils.getReturnType(className, methodName);
							token.setReturnTypeAtt(returnType);
						} catch (Exception e) {
							System.out.println("Cannot get the class name!number:[" + stmt.line.number + "], text:[ "
									+ stmt.line.text.trim() + " ], type:[" + simpleName + "]");
							throw e;
						}

					}

				} else if (token.isMemberVar()) {

				}

			}

		}

	}

}

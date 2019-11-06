package com.sum.shy.core.analyzer;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Type;
import com.sum.shy.core.utils.ReflectUtils;

public class NativeInvokeGuide {

	public static void check(Clazz clazz, Stmt stmt) {
		for (int i = 0; i < stmt.size(); i++) {
			Token token = stmt.getToken(i);
			if (token.isInvoke()) {
				if (token.isInvokeStatic()) {
					String type = token.getClassNameAtt();
					String clazzName = clazz.findImport(type);
					String methodName = token.getStaticMethodNameAtt();
					Type returnType = ReflectUtils.getReturnType(clazzName, methodName);
					token.setReturnTypeAtt(returnType);
				} else if (token.isInvokeMember()) {

				}
			}
		}
	}

}

//// 如果是数组的get方法，则返回泛型
//Type returnType = token.getReturnTypeAtt();
//if (returnType != null) {
//	if (Constants.ARRAY_TYPE.equals(returnType.type)) {
//		String method = token.getMemberMethodNameAtt();
//		if ("get".equals(method)) {
//			List<String> genericTypes = token.getGenericTypesAtt();
//			return genericTypes.get(0);
//		}
//	} else if (Constants.MAP_TYPE.equals(returnType.type)) {
//		String method = token.getMemberMethodNameAtt();
//		if ("get".equals(method)) {
//			List<String> genericTypes = token.getGenericTypesAtt();
//			return genericTypes.get(1);
//		}
//	}
//}

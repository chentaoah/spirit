package com.sum.shy.core.analyzer;

import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.api.Listener;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;
import com.sum.shy.core.entity.Variable;

/**
 * 自动引入器
 * 
 * @author chentao26275
 *
 */
public class AutoImporter {

	public static void doImport(Map<String, CtClass> classes) {
		for (CtClass clazz : classes.values()) {
			for (Element element : clazz.getAllElement()) {
				if (element instanceof CtField) {
					importFieldType(clazz, (CtField) element);
				} else if (element instanceof CtMethod) {
					importMethodType(clazz, (CtMethod) element);
				}
			}
		}
	}

	private static void importFieldType(CtClass clazz, CtField field) {
		CodeType codeType = (CodeType) field.type;
		importByTypeName(clazz, codeType.getTypeName());
	}

	private static void importMethodType(CtClass clazz, CtMethod method) {
		CodeType codeType = (CodeType) method.returnType;
		importByTypeName(clazz, codeType.getTypeName());
		for (Param param : method.params) {
			codeType = (CodeType) param.type;
			importByTypeName(clazz, codeType.getTypeName());
		}

		FastIterator.traver(clazz, method, new Listener() {
			@Override
			public Object handle(CtClass clazz, CtMethod method, int depth, String block, Line line, Stmt stmt) {
				if (stmt.isDeclare()) {
					importByTypeName(clazz, stmt.get(0));

				} else if (stmt.isAssign()) {
					// 判断变量追踪是否帮我们找到了该变量的类型
					Token token = stmt.getToken(0);
					CodeType codeType = (CodeType) token.getTypeAtt();
					if (!codeType.isType()) {// 如果返回的不是一个type token,则进行深度推导
						codeType = InvokeVisiter.visitCodeType(clazz, codeType);
					}
					importByTypeName(clazz, codeType.getTypeName());// 将变量追加到上下文之后,添加类型到import里
				}
				return null;
			}
		});

	}

	private static void importByTypeName(CtClass clazz, String typeName) {
		if (isBasicType(typeName)) {// java的基本类型,不用引入
			return;

		} else if ("List".equals(typeName)) {
			clazz.addImport("java.util.List");

		} else if ("Map".equals(typeName)) {
			clazz.addImport("java.util.Map");

		} else {
			String className = clazz.findImport(typeName);
			if (Context.get().isFriend(className)) {// 如果是友元
				clazz.addImport(className);
			}
		}

	}

	private static boolean isBasicType(String typeName) {
		switch (typeName) {
		case "boolean":
			return true;
		case "int":
			return true;
		case "long":
			return true;
		case "double":
			return true;
		case "Object":
			return true;
		case "String":
			return true;
		default:
			return false;
		}
	}

}

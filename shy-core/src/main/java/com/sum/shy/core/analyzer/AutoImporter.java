package com.sum.shy.core.analyzer;

import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.api.Handler;
import com.sum.shy.core.api.Type;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.core.entity.Token;

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
				importElement(clazz, element);
			}
		}
	}

	private static void importElement(CtClass clazz, Element element) {
		if (element instanceof CtField) {
			importType(clazz, element.getType());

		} else if (element instanceof CtMethod) {
			importType(clazz, element.getType());// 方法返回类型
			CtMethod method = (CtMethod) element;
			// 自动引入参数中的一些类型
			for (Param param : method.params) {
				importType(clazz, param.type);
			}

			// 自动引入方法体中的一些类型
			MethodResolver.resolve(clazz, method, new Handler() {
				@Override
				public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line,
						Stmt stmt) {
					if (stmt.isDeclare()) {
						importType(clazz, new CodeType(clazz, stmt.getToken(0)));

					} else if (stmt.isAssign()) {
						Token token = stmt.getToken(0);
						importType(clazz, token.getTypeAtt());
					}
					return null;
				}
			});
		}
	}

	private static void importType(CtClass clazz, Type type) {
		clazz.addImport(type.getClassName());
		for (String genericType : type.getGenericTypes()) {
			clazz.addImport(genericType);
		}
	}

}

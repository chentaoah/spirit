package com.sum.shy.core.analyzer;

import java.util.Map;

import com.sum.shy.core.api.Element;
import com.sum.shy.core.entity.CodeType;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Token;

/**
 * 自动引入器
 * 
 * @author chentao26275
 *
 */
public class AutoImporter {

	public static void autoImport(Map<String, CtClass> classes) {
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
		if (codeType.isType()) {
			Token token = codeType.token;
			String typeName = token.value.toString();
			importByTypeName(clazz, typeName);
		}
	}

	private static void importMethodType(CtClass clazz, CtMethod method) {

	}

	private static void importByTypeName(CtClass clazz, String typeName) {
		if ("String".equals(typeName)) {// java的基本类型,不用引入
			return;
		} else {

		}

	}

}

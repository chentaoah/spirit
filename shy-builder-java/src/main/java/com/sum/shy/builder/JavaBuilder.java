package com.sum.shy.builder;

import com.sum.shy.api.CodeBuilder;
import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.clazz.Method;
import com.sum.shy.clazz.Param;
import com.sum.shy.sentence.Sentence;

public class JavaBuilder implements CodeBuilder {

	public String build(Clazz clazz) {

		System.out.println("");
		System.out.println("========================java========================");

		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package " + clazz.packageStr + ";\n");

		sb.append("\n");
		// import
		for (String importStr : clazz.importStrs) {
			sb.append("import " + importStr + ";\n");
		}
		sb.append("\n");
		// class
		sb.append("public class " + clazz.className + " ");
		if (clazz.superName != null) {
			sb.append("extends " + clazz.superName + " ");
		}
		if (clazz.interfaces.size() > 0) {
			sb.append("implements ");
			for (String inf : clazz.interfaces) {
				sb.append(inf + ",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
		}

		sb.append("{\n");
		for (Field field : clazz.staticFields) {
			sb.append("\tpublic static " + field.type + " " + field.name + " = " + convertRightValue(field.sentence)
					+ ";\n");
		}
		for (Field field : clazz.fields) {
			sb.append("\tpublic " + field.type + " " + field.name + " = " + convertRightValue(field.sentence) + ";\n");
		}

		sb.append("\n");

		for (Method method : clazz.staticMethods) {
			sb.append("\tpublic static " + method.returnType + " " + method.name + "(");
			for (Param param : method.params) {
				sb.append(param.type + " " + param.name + ",");
			}
			if (method.params.size() > 0) {
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			convertMethod(sb, method);

			sb.append("\t}\n");
			sb.append("\n");
		}
		for (Method method : clazz.methods) {
			sb.append("\tpublic " + method.returnType + " " + method.name + "(");
			for (Param param : method.params) {
				sb.append(param.type + " " + param.name + ",");
			}
			if (method.params.size() > 0) {
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			convertMethod(sb, method);

			sb.append("\t}\n");
			sb.append("\n");
		}

		sb.append("}");

		System.out.println(sb.toString());

		return null;

	}

	private String convertRightValue(Sentence sentence) {
		return null;
	}

	private void convertMethod(StringBuilder sb, Method method) {
		for (String line : method.methodLines) {
			sb.append("\t\t" + line.trim() + "\n");
		}
	}

}

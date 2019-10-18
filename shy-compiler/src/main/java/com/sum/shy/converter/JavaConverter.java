package com.sum.shy.converter;

import com.sum.shy.entity.SClass;
import com.sum.shy.entity.SField;
import com.sum.shy.entity.SMethod;
import com.sum.shy.entity.SParam;

public class JavaConverter {

	public void convert(SClass clazz) {

		StringBuilder sb = new StringBuilder();
		// package
		sb.append("package " + clazz.packageStr + ";\n");
		// import
		for (String importStr : clazz.importStrs) {
			sb.append("import " + importStr + ";\n");
		}
		// class
		sb.append("public class " + clazz.className + " ");
		if (clazz.superClass != null) {
			sb.append("extends " + clazz.superClass + " ");
		}
		if (clazz.interfaces.size() > 0) {
			sb.append("implements ");
		}
		for (String inf : clazz.interfaces) {
			sb.append(inf + ",");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(" {\n");
		for (SField field : clazz.staticFields) {
			sb.append("public static" + field.type + " " + field.name + "=" + field.value + ";\n");
		}
		for (SField field : clazz.fields) {
			sb.append("public " + field.type + " " + field.name + "=" + field.value + ";\n");
		}
		for (SMethod method : clazz.staticMethods) {
			sb.append("public static" + method.returnType + " " + method.name + "(");
			for (SParam param : method.params) {
				sb.append(param.type + " " + param.name + ",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("){\n");

			convertMethod(sb, method);

			sb.append("}");
		}
		for (SMethod method : clazz.methods) {
			sb.append("public " + method.returnType + " " + method.name + "(");
			for (SParam param : method.params) {
				sb.append(param.type + " " + param.name + ",");
			}
			sb.deleteCharAt(sb.lastIndexOf(","));
			sb.append("){\n");

			convertMethod(sb, method);

			sb.append("}");
		}

		sb.append("}");

		System.out.println(sb.toString());

	}

	private void convertMethod(StringBuilder sb, SMethod method) {
		for (String line : method.methodLines) {
			sb.append(line);
		}
	}

}

package com.sum.shy.builder;

import com.sum.shy.api.CodeBuilder;
import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.clazz.Method;
import com.sum.shy.clazz.Param;
import com.sum.shy.sentence.Morpheme;
import com.sum.shy.sentence.Sentence;

public class JavaBuilder implements CodeBuilder {

	public String build(Clazz clazz) {

		System.out.println("========================java========================");

		StringBuilder sb = new StringBuilder();

		// ============================ head ================================

		// package
		sb.append("package " + clazz.packageStr + ";\n");
		sb.append("\n");
		// import
		for (String importStr : clazz.importStrs) {
			sb.append("import " + importStr + ";\n");
		}
		sb.append("\n");

		// ============================ class ================================

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

		// ============================ field ================================

		for (Field field : clazz.staticFields) {
			sb.append("\tpublic static " + convertType(field.type) + " " + field.name + " = "
					+ buildRightValue(field.sentence) + ";\n");
		}
		for (Field field : clazz.fields) {
			sb.append("\tpublic " + convertType(field.type) + " " + field.name + " = " + buildRightValue(field.sentence)
					+ ";\n");
		}
		sb.append("\n");

		// ============================ method ================================

		for (Method method : clazz.staticMethods) {
			sb.append("\tpublic static " + method.returnType + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			buildMethod(sb, clazz, method);

			sb.append("\t}\n");
			sb.append("\n");
		}
		for (Method method : clazz.methods) {
			sb.append("\tpublic " + method.returnType + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			buildMethod(sb, clazz, method);

			sb.append("\t}\n");
			sb.append("\n");
		}

		sb.append("}");

		return sb.toString();

	}

	private String convertType(String type) {
		if ("str".equals(type)) {
			return "String";
		} else if ("array".equals(type)) {
			return "List<Object>";
		} else if ("map".equals(type)) {
			return "Map<Object,Object>";
		}
		return type;
	}

	private String buildRightValue(Sentence sentence) {
		StringBuilder sb = new StringBuilder();
		for (int i = 2; i < sentence.units.size(); i++) {
			String str = sentence.getUnit(i);
			if (Morpheme.isInitInvoke(str)) {
				sb.append("new " + str);
			} else {
				sb.append(str);
			}
		}
		return sb.toString();
	}

	private void buildMethod(StringBuilder sb, Clazz clazz, Method method) {
		for (String line : method.methodLines) {
			sb.append("\t\t" + line.trim() + "\n");
		}
	}

}

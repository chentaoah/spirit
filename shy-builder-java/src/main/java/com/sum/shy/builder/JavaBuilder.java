package com.sum.shy.builder;

import java.util.Map;

import com.sum.shy.api.CodeBuilder;
import com.sum.shy.clazz.Clazz;
import com.sum.shy.clazz.Field;
import com.sum.shy.clazz.Method;
import com.sum.shy.clazz.Param;
import com.sum.shy.sentence.Analyzer;
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
			sb.append("\tpublic static " + convertType(field) + " " + convertSentence(clazz.defTypes, field.sentence)
					+ ";\n");
		}
		for (Field field : clazz.fields) {
			sb.append("\tpublic " + convertType(field) + " " + convertSentence(clazz.defTypes, field.sentence) + ";\n");
		}
		sb.append("\n");

		// ============================ method ================================

		for (Method method : clazz.staticMethods) {
			sb.append("\tpublic static " + convertReturnType(method.returnType) + " " + method.name + "(");
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
			sb.append("\tpublic " + convertReturnType(method.returnType) + " " + method.name + "(");
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

	private String convertType(Field field) {
		if ("str".equals(field.type)) {
			return "String";
		} else if ("array".equals(field.type)) {
			return "List<" + convertGenericType(field.genericTypes.get(0)) + ">";
		} else if ("map".equals(field.type)) {
			return "Map<" + convertGenericType(field.genericTypes.get(0)) + ","
					+ convertGenericType(field.genericTypes.get(1)) + ">";
		}
		return field.type;
	}

	private String convertGenericType(String str) {
		if ("boolean".equals(str)) {
			return "Boolean";
		} else if ("int".equals(str)) {
			return "Integer";
		} else if ("double".equals(str)) {
			return "Double";
		} else if ("str".equals(str)) {
			return "String";
		} else {
			return str;
		}
	}

	private String convertSentence(Map<String, String> defTypes, Sentence sentence) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < sentence.units.size(); i++) {
			String str = sentence.getUnit(i);
			if (":".equals(str)) {
				sb.append(",");
			} else if (Analyzer.isInvoke(str)) {
				if (Analyzer.isInitInvoke(str)) {
					sb.append("new ");
				}
				sb.append(str.substring(0, str.indexOf("(")) + "(");
				sb.append(convertSentence(defTypes, (Sentence) sentence.getSubSentence(i)));
				sb.append(")");
			} else {
				String type = Analyzer.getType(defTypes, str);
				if ("array".equals(type)) {
					sb.append("Collection.newArrayList(");
					sb.append(convertSentence(defTypes, (Sentence) sentence.getSubSentence(i)));
					sb.append(")");
				} else if ("map".equals(type)) {
					sb.append("Collection.newHashMap(");
					sb.append(convertSentence(defTypes, (Sentence) sentence.getSubSentence(i)));
					sb.append(")");
				} else {
					sb.append(str);
				}

			}

		}
		return sb.toString();
	}

	private String convertReturnType(String str) {
		if ("var".equals(str)) {
			return "void";
		} else {
			return str;
		}
	}

	private void buildMethod(StringBuilder sb, Clazz clazz, Method method) {
		for (String line : method.methodLines) {
			sb.append("\t\t" + line.trim() + "\n");
		}
	}

}

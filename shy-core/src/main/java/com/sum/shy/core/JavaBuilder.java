package com.sum.shy.core;

import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Method;

public class JavaBuilder {

	public String build(Clazz clazz) {
		return null;

//		System.out.println("========================java========================");
//
//		StringBuilder sb = new StringBuilder();
//
//		// ============================ head ================================
//
//		// package
//		sb.append("package " + clazz.packageStr + ";\n");
//		sb.append("\n");
//		// import
//		for (String importStr : clazz.importStrs) {
//			sb.append("import " + importStr + ";\n");
//		}
//		sb.append("\n");
//
//		// ============================ class ================================
//
//		// class
//		sb.append("public class " + clazz.className + " ");
//		if (clazz.superName != null) {
//			sb.append("extends " + clazz.superName + " ");
//		}
//		if (clazz.interfaces.size() > 0) {
//			sb.append("implements ");
//			for (String inf : clazz.interfaces) {
//				sb.append(inf + ",");
//			}
//			sb.deleteCharAt(sb.lastIndexOf(","));
//		}
//		sb.append("{\n");
//
//		// ============================ field ================================
//
//		for (Field field : clazz.staticFields) {
//			sb.append("\tpublic static " + convertType(field) + " " + convertSentence(clazz.defTypes, field.stmt)
//					+ ";\n");
//		}
//		for (Field field : clazz.fields) {
//			sb.append("\tpublic " + convertType(field) + " " + convertSentence(clazz.defTypes, field.stmt) + ";\n");
//		}
//		sb.append("\n");
//
//		// ============================ method ================================
//
//		for (Method method : clazz.staticMethods) {
//			sb.append("\tpublic static " + convertReturnType(method.returnType) + " " + method.name + "(");
//			if (method.params.size() > 0) {
//				for (Param param : method.params) {
//					sb.append(param.type + " " + param.name + ",");
//				}
//				sb.deleteCharAt(sb.lastIndexOf(","));
//			}
//			sb.append("){\n");
//
//			convertMethodLine(sb, clazz, method);
//
//			sb.append("\t}\n");
//			sb.append("\n");
//		}
//		for (Method method : clazz.methods) {
//			sb.append("\tpublic " + convertReturnType(method.returnType) + " " + method.name + "(");
//			if (method.params.size() > 0) {
//				for (Param param : method.params) {
//					sb.append(param.type + " " + param.name + ",");
//				}
//				sb.deleteCharAt(sb.lastIndexOf(","));
//			}
//			sb.append("){\n");
//
//			convertMethodLine(sb, clazz, method);
//
//			sb.append("\t}\n");
//			sb.append("\n");
//		}
//
//		sb.append("}");
//
//		return sb.toString();

	}

//	private String convertType(Field field) {
//		if ("str".equals(field.type)) {
//			return "String";
//		} else if ("array".equals(field.type)) {
//			return "List<" + convertGenericType(field.genericTypes.get(0)) + ">";
//		} else if ("map".equals(field.type)) {
//			return "Map<" + convertGenericType(field.genericTypes.get(0)) + ","
//					+ convertGenericType(field.genericTypes.get(1)) + ">";
//		}
//		return field.type;
//	}
//
//	private String convertType(String str) {
//		if ("str".equals(str)) {
//			return "String";
//		}
//		return str;
//	}
//
//	private String convertGenericType(String str) {
//		if ("boolean".equals(str)) {
//			return "Boolean";
//		} else if ("int".equals(str)) {
//			return "Integer";
//		} else if ("double".equals(str)) {
//			return "Double";
//		} else if ("str".equals(str)) {
//			return "String";
//		} else {
//			return str;
//		}
//	}
//
//	private String convertSentence(Map<String, String> defTypes, Stmt stmt) {
//		StringBuilder sb = new StringBuilder();
//		for (int i = 0; i < stmt.units.size(); i++) {
//			String str = stmt.getUnit(i);
//			if (":".equals(str)) {
//				sb.append(",");
//			} else if (Analyzer.isInvoke(str)) {
//				if (Analyzer.isInitInvoke(str)) {
//					sb.append("new ");
//				}
//				sb.append(str.substring(0, str.indexOf("(")) + "(");
//				sb.append(convertSentence(defTypes, (Stmt) stmt.getSubSentence(i)));
//				sb.append(")");
//			} else {
//				String type = Analyzer.getType(defTypes, str);
//				if ("array".equals(type)) {
//					sb.append("Collection.newArrayList(");
//					sb.append(convertSentence(defTypes, (Stmt) stmt.getSubSentence(i)));
//					sb.append(")");
//				} else if ("map".equals(type)) {
//					sb.append("Collection.newHashMap(");
//					sb.append(convertSentence(defTypes, (Stmt) stmt.getSubSentence(i)));
//					sb.append(")");
//				} else {
//					sb.append(str);
//				}
//
//			}
//
//		}
//		return sb.toString();
//	}
//
//	private String convertReturnType(String str) {
//		if ("var".equals(str)) {
//			return "void";
//		} else {
//			return str;
//		}
//	}

	private void convertMethodLine(StringBuilder sb, Clazz clazz, Method method) {
//		for (String line : method.methodLines) {
//			if (line.trim().startsWith("//") || line.trim().length() == 0) {
//				continue;
//			}
//			Stmt stmt = new Stmt(line);
//			try {
//				if ("if".equals(stmt.getUnit(0))) {
//					stmt.units.add(1, "(");
//					stmt.units.add(stmt.units.size() - 1, ")");
//					sb.append("\t\t" + stmt + "\n");
//				} else if ("else".equals(stmt.getUnit(1)) && "if".equals(stmt.getUnit(2))) {
//					stmt.units.add(3, "(");
//					stmt.units.add(stmt.units.size() - 1, ")");
//					sb.append("\t\t" + stmt + "\n");
//				} else if ("=".equals(stmt.getUnit(1))) {
//					sb.append("\t\t" + convertType(Analyzer.getType(clazz.defTypes, stmt)) + " "
//							+ convertSentence(clazz.defTypes, stmt) + ";\n");
//				} else if ("}".equals(stmt.getUnit(0))) {
//					sb.append("\t\t" + stmt + "\n");
//				} else {
//					sb.append("\t\t" + stmt + ";\n");
//				}
//			} catch (Exception e) {
//				// ignore
//			}
//
//		}
	}

}

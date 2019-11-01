package com.sum.shy.core;

import java.util.List;

import com.sum.shy.core.api.Converter;
import com.sum.shy.core.converter.InvokeConverter;
import com.sum.shy.core.converter.ReturnConverter;
import com.sum.shy.core.converter.AbstractConverter;
import com.sum.shy.core.converter.AssignmentConverter;
import com.sum.shy.core.converter.EndConverter;
import com.sum.shy.core.converter.IfConverter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;

public class JavaBuilder {

	static {
		// 赋值语句和方法调用语句都使用一个转换器
		Converter.register("return", new ReturnConverter());
		Converter.register("assignment", new AssignmentConverter());
		Converter.register("invoke", new InvokeConverter());
		Converter.register("if", new IfConverter());
		Converter.register("end", new EndConverter());// }结束符
	}

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
			sb.append("\tpublic static " + convertType(field.type, field.genericTypes) + " "
					+ AbstractConverter.convertStmt(field.stmt) + ";\n");
		}
		for (Field field : clazz.fields) {
			sb.append("\tpublic " + convertType(field.type, field.genericTypes) + " "
					+ AbstractConverter.convertStmt(field.stmt) + ";\n");
		}
		sb.append("\n");

		// ============================ method ================================

		for (Method method : clazz.staticMethods) {
			sb.append(
					"\tpublic static " + convertType(method.returnType, method.genericTypes) + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			convertMethodLine(sb, clazz, method);

			sb.append("\t}\n");
			sb.append("\n");
		}

		for (Method method : clazz.methods) {
			sb.append("\tpublic " + convertType(method.returnType, method.genericTypes) + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");

			convertMethodLine(sb, clazz, method);

			sb.append("\t}\n");
			sb.append("\n");
		}

		sb.append("}");

		return sb.toString();

	}

	private String convertType(String type, List<String> genericTypes) {
		if ("str".equals(type)) {
			return "String";
		} else if ("array".equals(type)) {
			return "List<" + convertGenericType(genericTypes.get(0)) + ">";
		} else if ("map".equals(type)) {
			return "Map<" + convertGenericType(genericTypes.get(0)) + "," + convertGenericType(genericTypes.get(1))
					+ ">";
		} else if ("unknown".equals(type)) {
			return "void";
		}
		return type;
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

	private void convertMethodLine(StringBuilder sb, Clazz clazz, Method method) {
		Context.get().scope = "method";
		List<String> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (line.trim().startsWith("//") || line.trim().length() == 0) {
				continue;
			}
			Stmt stmt = Stmt.create(line);
			Converter converter = Converter.get(stmt.syntax);
			int jump = converter.convert(sb, "1", "\t\t", clazz, method, lines, i, line, stmt);
			i = i + jump;

		}

	}

}

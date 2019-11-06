package com.sum.shy.core;

import java.util.List;

import com.sum.shy.core.api.Converter;
import com.sum.shy.core.converter.InvokeConverter;
import com.sum.shy.core.converter.ReturnConverter;
import com.sum.shy.core.converter.AbstractConverter;
import com.sum.shy.core.converter.AssignmentConverter;
import com.sum.shy.core.converter.DeclareConverter;
import com.sum.shy.core.converter.EndConverter;
import com.sum.shy.core.converter.IfConverter;
import com.sum.shy.core.entity.Clazz;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.Field;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.Method;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;

public class JavaBuilder {

	static {
		// 赋值语句和方法调用语句都使用一个转换器
		Converter.register("declare", new DeclareConverter());
		Converter.register("assignment", new AssignmentConverter());
		Converter.register("invoke", new InvokeConverter());
		Converter.register("if", new IfConverter());
		Converter.register("return", new ReturnConverter());
		Converter.register("end", new EndConverter());// }结束符

	}

	public String build(Clazz clazz) {

		System.out.println(
				"=================================== Java ========================================");

		StringBuilder sb = new StringBuilder();

		// ============================ head ================================

		// package
		sb.append("package " + clazz.packageStr + ";\n");
		sb.append("\n");
		sb.append("import java.util.List;\n");
		sb.append("import java.util.Map;\n");
		sb.append("import com.sum.shy.library.Collection;\n");
		sb.append("import com.sum.shy.library.StringUtils;\n");
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
			sb.append("\tpublic static " + AbstractConverter.convertType(field.type) + " "
					+ AbstractConverter.convertStmt(field.stmt) + ";\n");
		}
		for (Field field : clazz.fields) {
			sb.append("\tpublic " + AbstractConverter.convertType(field.type) + " "
					+ AbstractConverter.convertStmt(field.stmt) + ";\n");
		}
		sb.append("\n");

		// ============================ method ================================

		for (Method method : clazz.staticMethods) {
			// 先尝试拼接方法内容
			StringBuilder methodContent = new StringBuilder();
			convertMethodLine(methodContent, clazz, method);

			sb.append("\tpublic static " + AbstractConverter.convertType(method.returnType) + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type.name + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");
			sb.append(methodContent);
			sb.append("\t}\n");
			sb.append("\n");
		}

		for (Method method : clazz.methods) {
			// 先尝试拼接方法内容
			StringBuilder methodContent = new StringBuilder();
			convertMethodLine(methodContent, clazz, method);

			sb.append("\tpublic " + AbstractConverter.convertType(method.returnType) + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					sb.append(param.type.name + " " + param.name + ",");
				}
				sb.deleteCharAt(sb.lastIndexOf(","));
			}
			sb.append("){\n");
			sb.append(methodContent);
			sb.append("\t}\n");
			sb.append("\n");
		}

		sb.append("}");

		return sb.toString();

	}

	private void convertMethodLine(StringBuilder sb, Clazz clazz, Method method) {
		Context.get().scope = "method";
		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			Converter converter = Converter.get(stmt.syntax);
			int jump = converter.convert(sb, "1", "\t\t", clazz, method, lines, i, line, stmt);
			i = i + jump;

		}

	}

}

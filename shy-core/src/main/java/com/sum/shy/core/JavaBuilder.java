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
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
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

	public String build(CtClass clazz) {

		System.out.println();
		System.out.println("=================================== Java ========================================");

//		clazz.addImport("java.util.List");
//		clazz.addImport("java.util.Map");

		// ============================ class ================================

		StringBuilder body = new StringBuilder();

		// class
		body.append("public class " + clazz.className + " ");
		if (clazz.superName != null) {
			body.append("extends " + clazz.superName + " ");
		}
		if (clazz.interfaces.size() > 0) {
			body.append("implements ");
			for (String inf : clazz.interfaces) {
				body.append(inf + ",");
			}
			body.deleteCharAt(body.lastIndexOf(","));
		}
		body.append("{\n");
		body.append("\n");

		// ============================ field ================================

		for (CtField field : clazz.staticFields) {
			body.append("\tpublic static " + AbstractConverter.convertType(clazz, field.type) + " "
					+ AbstractConverter.convertStmt(clazz, field.stmt) + ";\n");
		}

		for (CtField field : clazz.fields) {
			body.append("\tpublic " + AbstractConverter.convertType(clazz, field.type) + " "
					+ AbstractConverter.convertStmt(clazz, field.stmt) + ";\n");
		}

		body.append("\n");

		// ============================ method ================================

		for (CtMethod method : clazz.staticMethods) {
			body.append("\tpublic static " + AbstractConverter.convertType(clazz, method.returnType) + " " + method.name
					+ "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					body.append(AbstractConverter.convertType(clazz, param.type) + " " + param.name + ",");
				}
				body.deleteCharAt(body.lastIndexOf(","));
			}
			body.append("){\n");
			convertMethod(body, clazz, method);
			body.append("\t}\n");
			body.append("\n");
		}

		for (CtMethod method : clazz.methods) {
			body.append(
					"\tpublic " + AbstractConverter.convertType(clazz, method.returnType) + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					body.append(AbstractConverter.convertType(clazz, param.type) + " " + param.name + ",");
				}
				body.deleteCharAt(body.lastIndexOf(","));
			}
			body.append("){\n");
			convertMethod(body, clazz, method);
			body.append("\t}\n");
			body.append("\n");
		}

		body.append("}");

		// ============================ head ================================

		StringBuilder head = new StringBuilder();

		// package
		head.append("package " + clazz.packageStr + ";\n");
		head.append("\n");
		// import
		for (String importStr : clazz.importStrs.values()) {
			head.append("import " + importStr + ";\n");
		}

		head.append("\n");

		return head.append(body).toString();

	}

	public static void convertMethod(StringBuilder sb, CtClass clazz, CtMethod method) {

		Context.get().scope = "method";

		List<Line> lines = method.methodLines;
		for (int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			if (line.isIgnore())
				continue;

			Stmt stmt = Stmt.create(line);
			Converter converter = Converter.get(stmt.syntax);
			int jump = converter.convert(sb, "0", "\t\t", clazz, method, lines, i, line, stmt);
			i = i + jump;

		}

	}

}

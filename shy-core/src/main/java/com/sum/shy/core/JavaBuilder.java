package com.sum.shy.core;

import java.util.List;

import com.sum.shy.core.analyzer.InvocationVisitor;
import com.sum.shy.core.analyzer.TypeDerivator;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.api.Type;
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

		System.out.println("=================================== Java ========================================");

		clazz.addImport("java.util.List");
		clazz.addImport("java.util.Map");

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

		// ============================ field ================================

		for (Field field : clazz.staticFields) {

			convertField(clazz, field);

			body.append("\tpublic static " + field.type.toString() + " "
					+ AbstractConverter.convertStmt(clazz, field.stmt) + ";\n");
		}

		for (Field field : clazz.fields) {

			convertField(clazz, field);

			body.append("\tpublic " + field.type.toString() + " " + AbstractConverter.convertStmt(clazz, field.stmt)
					+ ";\n");
		}

		body.append("\n");

		// ============================ method ================================

		for (Method method : clazz.staticMethods) {
			// 先尝试拼接方法内容
			StringBuilder methodContent = new StringBuilder();
			convertMethod(methodContent, clazz, method);

			body.append("\tpublic static " + method.returnType.toString() + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					body.append(param.type.toString() + " " + param.name + ",");
				}
				body.deleteCharAt(body.lastIndexOf(","));
			}
			body.append("){\n");
			body.append(methodContent);
			body.append("\t}\n");
			body.append("\n");
		}

		for (Method method : clazz.methods) {
			// 先尝试拼接方法内容
			StringBuilder methodContent = new StringBuilder();
			convertMethod(methodContent, clazz, method);

			body.append("\tpublic " + method.returnType.toString() + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					body.append(param.type.toString() + " " + param.name + ",");
				}
				body.deleteCharAt(body.lastIndexOf(","));
			}
			body.append("){\n");
			body.append(methodContent);
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

	private void convertField(Clazz clazz, Field field) {
		// 方法返回值推算
		InvocationVisitor.check(clazz, field.stmt);
		// 类型推导
		Type nativeType = TypeDerivator.getType(field.stmt);
		// 添加到头部类型引入(可以重复添加)
		clazz.addImport(nativeType);

		field.type = nativeType;

	}

	public static void convertMethod(StringBuilder sb, Clazz clazz, Method method) {
		Context.get().scope = "method";
		String dependency = clazz.packageStr + "." + clazz.className + "." + method.name;
		Context.get().addDependency(dependency);// 添加依赖

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

		Context.get().removeDependency(dependency);// 移除依赖

	}

}

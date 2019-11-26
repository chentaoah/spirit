package com.sum.shy.core;

import com.sum.shy.core.analyzer.MethodResolver;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.api.Handler;
import com.sum.shy.core.converter.DefaultConverter;
import com.sum.shy.core.converter.ForConverter;
import com.sum.shy.core.converter.ForInConverter;
import com.sum.shy.core.converter.NoneConverter;
import com.sum.shy.core.converter.PrintConverter;
import com.sum.shy.core.converter.AssignConverter;
import com.sum.shy.core.converter.CatchConverter;
import com.sum.shy.core.converter.ConditionConverter;
import com.sum.shy.core.converter.DeclareConverter;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Param;
import com.sum.shy.core.entity.Stmt;

public class JavaBuilder {

	static {
		// 赋值语句和方法调用语句都使用一个转换器
		Converter.register("declare", new DeclareConverter());// 声明转换

		Converter.register("assign", new AssignConverter());// 赋值转换

		Converter.register("if", new ConditionConverter());// 条件转换
		Converter.register("elseif", new ConditionConverter());
		Converter.register("else", new NoneConverter());// 什么都不做
		Converter.register("end", new NoneConverter());// 什么都不做

		Converter.register("while", new ConditionConverter());// while循环

		Converter.register("try", new NoneConverter());// 什么都不做
		Converter.register("catch", new CatchConverter());// catch语句

		Converter.register("for", new ForConverter());// for语句
		Converter.register("for_in", new ForInConverter());// for in语句

		Converter.register("print", new PrintConverter());// 日志
		Converter.register("debug", new PrintConverter());// 日志

		Converter.register("return", new DefaultConverter());// 返回

	}

	public String build(CtClass clazz) {

		System.out.println();
		System.out.println("=================================== Java ========================================");

		// 构建java代码是倒过来的,这样能够在构建的时候,再重建一部分class信息
		String methods = buildMethods(clazz);

		String fields = buildFields(clazz);

		String classStr = buildClass(clazz);

		String head = buildHead(clazz);

		return head + classStr + fields + methods;

	}

	private String buildHead(CtClass clazz) {

		StringBuilder body = new StringBuilder();

		// package
		body.append("package " + clazz.packageStr + ";\n");
		body.append("\n");
		// import
		for (String importStr : clazz.importStrs.values()) {
			body.append("import " + importStr + ";\n");
		}

		body.append("\n");

		return body.toString();
	}

	private String buildClass(CtClass clazz) {

		StringBuilder body = new StringBuilder();
		// class
		body.append("public class " + clazz.typeName + " ");
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

		return body.toString();
	}

	private String buildFields(CtClass clazz) {

		StringBuilder body = new StringBuilder();

		for (CtField field : clazz.staticFields) {
			body.append(
					"\tpublic static " + field.type + " " + DefaultConverter.convertSubStmt(clazz, field.stmt) + ";\n");
		}

		for (CtField field : clazz.fields) {
			body.append("\tpublic " + field.type + " " + DefaultConverter.convertSubStmt(clazz, field.stmt) + ";\n");
		}

		body.append("\n");

		return body.toString();
	}

	private String buildMethods(CtClass clazz) {

		StringBuilder body = new StringBuilder();

		for (CtMethod method : clazz.staticMethods) {
			body.append("\tpublic static " + method.type + " " + method.name + "(");
			// 主方法自动加参数
			if ("main".equals(method.name)) {
				body.append("String[] args");
			} else {
				if (method.params.size() > 0) {
					for (Param param : method.params) {
						body.append(param.type + " " + param.name + ", ");
					}
					body.delete(body.lastIndexOf(","), body.length());
				}
			}
			body.append("){\n");
			convertMethod(body, clazz, method);
			body.append("\t}\n");
			body.append("\n");
		}

		for (CtMethod method : clazz.methods) {
			body.append("\tpublic " + method.type + " " + method.name + "(");
			if (method.params.size() > 0) {
				for (Param param : method.params) {
					body.append(param.type + " " + param.name + ", ");
				}
				body.delete(body.lastIndexOf(","), body.length());
			}
			body.append("){\n");
			convertMethod(body, clazz, method);
			body.append("\t}\n");
			body.append("\n");
		}

		body.append("}");

		return body.toString();
	}

	public static void convertMethod(StringBuilder sb, CtClass clazz, CtMethod method) {
		MethodResolver.resolve(clazz, method, new Handler() {
			@Override
			public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
				Converter converter = Converter.get(stmt.syntax);
				stmt = converter.convert(clazz, method, indent, block, line, stmt);
				sb.append(indent + stmt + "\n");
				return null;// 必须返回null,才能够持续进行下去
			}
		});

	}

}

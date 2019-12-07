package com.sum.shy.core;

import com.google.common.base.Joiner;
import com.sum.shy.core.analyzer.MethodResolver;
import com.sum.shy.core.api.Annotated;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.api.Handler;
import com.sum.shy.core.converter.DefaultConverter;
import com.sum.shy.core.converter.FastAddConverter;
import com.sum.shy.core.converter.ForConverter;
import com.sum.shy.core.converter.ForInConverter;
import com.sum.shy.core.converter.JudgeInvokeConverter;
import com.sum.shy.core.converter.NoneConverter;
import com.sum.shy.core.converter.PrintConverter;
import com.sum.shy.core.converter.SyncConverter;
import com.sum.shy.core.converter.AssignConverter;
import com.sum.shy.core.converter.CatchConverter;
import com.sum.shy.core.converter.ConditionConverter;
import com.sum.shy.core.converter.DeclareConverter;
import com.sum.shy.core.entity.Context;
import com.sum.shy.core.entity.CtClass;
import com.sum.shy.core.entity.CtField;
import com.sum.shy.core.entity.Line;
import com.sum.shy.core.entity.CtMethod;
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.library.StringUtils;

public class JavaBuilder {

	static {

		Converter.register("annotation", new DefaultConverter());// 注解

		Converter.register("declare", new DeclareConverter());// 声明转换
		Converter.register("assign", new AssignConverter());// 赋值转换
		Converter.register("invoke", new DefaultConverter());// 方法调用

		Converter.register("if", new ConditionConverter());// 条件转换
		Converter.register("elseif", new ConditionConverter());
		Converter.register("else", new NoneConverter());// 什么都不做
		Converter.register("end", new NoneConverter());// 什么都不做

		Converter.register("for", new ForConverter());// for语句
		Converter.register("for_in", new ForInConverter());// for in语句

		Converter.register("while", new ConditionConverter());// while循环
		Converter.register("continue", new DefaultConverter());// continue
		Converter.register("break", new DefaultConverter());// break

		Converter.register("try", new NoneConverter());// 什么都不做
		Converter.register("catch", new CatchConverter());// catch语句
		Converter.register("throw", new DefaultConverter());// throw

		Converter.register("sync", new SyncConverter());// 同步语句

		Converter.register("print", new PrintConverter());// 日志
		Converter.register("debug", new PrintConverter());// 日志

		Converter.register("fast_add", new FastAddConverter());// 快速添加

		Converter.register("judge_invoke", new JudgeInvokeConverter());// 判空调用

		Converter.register("return", new DefaultConverter());// 返回

	}

	public String build(CtClass clazz) {
		// 当前类
		Context.get().currentClass = clazz;

		System.out.println();
		System.out.println("=================================== Java ========================================");

		if ("interface".equals(clazz.category)) {
			String head = buildHead(clazz);
			String classStr = buildInterface(clazz);
			return head + classStr;

		} else {
			// 构建java代码是倒过来的,这样能够在构建的时候,再重建一部分class信息
			String methods = buildMethods(clazz);
			String fields = buildFields(clazz);
			String classStr = buildClass(clazz, fields, methods);
			String head = buildHead(clazz);
			return head + classStr;

		}

	}

	/**
	 * 构建头部
	 * 
	 * @param clazz
	 * @return
	 */
	private String buildHead(CtClass clazz) {

		StringBuilder body = new StringBuilder();
		body.append(String.format("package %s;\n", clazz.packageStr));// package
		body.append("\n");
		for (String importStr : clazz.importStrs.values()) {// import
			body.append(String.format("import %s;\n", importStr));
		}
		// 分隔一下
		if (clazz.importStrs.size() > 0)
			body.append("\n");
		return body.toString();
	}

	private String buildInterface(CtClass clazz) {
		StringBuilder body = new StringBuilder();
		String extendsStr = clazz.interfaces.size() > 0
				? String.format("extends %s ", Joiner.on(", ").join(clazz.interfaces))
				: "";
		body.append(buildAnnotations("", clazz));// 构建上面的注解
		body.append(String.format("public interface %s%s{\n\n", clazz.typeName + " ", extendsStr));
		// 几乎不做任何处理
		for (Line line : clazz.classLines) {
			if (!line.isIgnore()) {
				if (line.text.trim().startsWith("@")) {
					body.append(line.text + "\n");
				} else {
					body.append(line.text + ";\n\n");
				}
			}
		}
		body.append("}\n");// 追加一个class末尾
		return body.toString();
	}

	/**
	 * 构建class
	 * 
	 * @param clazz
	 * @param methods
	 * @param fields
	 * @return
	 */
	private String buildClass(CtClass clazz, String fields, String methods) {
		StringBuilder body = new StringBuilder();
		String abstractStr = "abstract".equals(clazz.category) ? "abstract " : "";
		String extendsStr = clazz.superName != null ? String.format("extends %s ", clazz.superName) : "";
		String implementsStr = clazz.interfaces.size() > 0
				? String.format("implements %s ", Joiner.on(", ").join(clazz.interfaces))
				: "";
		body.append(buildAnnotations("", clazz));// 构建上面的注解
		body.append(String.format("public %sclass %s%s%s{\n\n", abstractStr, clazz.typeName + " ", extendsStr,
				implementsStr));
		body.append(fields);
		body.append(methods);
		body.append("}\n");// 追加一个class末尾
		return body.toString();
	}

	/**
	 * 构建注解
	 * 
	 * @param element
	 * @return
	 */
	private String buildAnnotations(String indent, Annotated annotated) {
		StringBuilder body = new StringBuilder();
		for (String annotation : annotated.getAnnotations()) {
			body.append(indent + annotation + "\n");
		}
		return body.toString();
	}

	/**
	 * 构建字段
	 * 
	 * @param clazz
	 * @return
	 */
	private String buildFields(CtClass clazz) {

		StringBuilder body = new StringBuilder();

		for (CtField field : clazz.staticFields) {
			body.append(buildAnnotations("\t", field));
			body.append(buildField(clazz, "static", field));
		}
		for (CtField field : clazz.fields) {
			body.append(buildAnnotations("\t", field));
			body.append(buildField(clazz, "", field));
		}
		// 分隔一下
		if (clazz.staticFields.size() > 0 || clazz.fields.size() > 0)
			body.append("\n");

		return body.toString();
	}

	/**
	 * 构建字段
	 * 
	 * @param clazz
	 * @param desc
	 * @param field
	 * @return
	 */
	private String buildField(CtClass clazz, String desc, CtField field) {
		if (StringUtils.isNotEmpty(desc)) {
			return String.format("\tpublic %s %s %s;\n", desc, field.type,
					DefaultConverter.convertSubStmt(clazz, field.stmt));
		} else {
			return String.format("\tpublic %s %s;\n", field.type, DefaultConverter.convertSubStmt(clazz, field.stmt));
		}

	}

	/**
	 * 构建方法体
	 * 
	 * @param clazz
	 * @return
	 */
	private String buildMethods(CtClass clazz) {

		StringBuilder body = new StringBuilder();
		for (CtMethod method : clazz.staticMethods) {
			body.append(buildAnnotations("\t", method));
			body.append(buildMethod(clazz, "static", method));
		}
		for (CtMethod method : clazz.methods) {
			body.append(buildAnnotations("\t", method));
			body.append(buildMethod(clazz, "", method));
		}
		return body.toString();
	}

	/**
	 * 构建方法
	 * 
	 * @param clazz
	 * @param desc
	 * @param field
	 * @return
	 */
	private String buildMethod(CtClass clazz, String desc, CtMethod method) {
		StringBuilder body = new StringBuilder();
		String paramStr = "";
		if ("static".equals(desc) && "main".equals(method.name)) {// 主方法自动加参数
			paramStr = "String[] args";
		} else if (method.params.size() > 0) {
			paramStr = Joiner.on(", ").join(method.params);
		}
		// 可能会抛出的异常
		String exceptions = method.exceptions.size() > 0
				? String.format("throws %s ", Joiner.on(", ").join(method.exceptions))
				: "";

		if (StringUtils.isNotEmpty(desc)) {
			body.append(
					String.format("\tpublic %s %s %s(%s) %s{\n", desc, method.type, method.name, paramStr, exceptions));
		} else {
			body.append(String.format("\tpublic %s %s(%s) %s{\n", method.type, method.name, paramStr, exceptions));
		}
		// 转化方法体
		convertMethod(body, clazz, method);
		body.append("\t}\n\n");
		return body.toString();
	}

	/**
	 * 转换每一行
	 * 
	 * @param body
	 * @param clazz
	 * @param method
	 */
	public static void convertMethod(StringBuilder body, CtClass clazz, CtMethod method) {
		MethodResolver.resolve(clazz, method, new Handler() {
			@Override
			public Object handle(CtClass clazz, CtMethod method, String indent, String block, Line line, Stmt stmt) {
				try {
					Converter converter = Converter.get(stmt.syntax);
					stmt = converter.convert(clazz, method, indent, block, line, stmt);
					body.append(indent + stmt + "\n");
					return null;// 必须返回null,才能够持续进行下去
				} catch (Exception e) {
					System.out.println(stmt);
					System.out.println(stmt.syntax);
					throw e;
				}
			}
		});

	}

}

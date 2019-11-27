package com.sum.shy.core;

import com.google.common.base.Joiner;
import com.sum.shy.core.analyzer.MethodResolver;
import com.sum.shy.core.api.Converter;
import com.sum.shy.core.api.Handler;
import com.sum.shy.core.converter.DefaultConverter;
import com.sum.shy.core.converter.FastAddConverter;
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
import com.sum.shy.core.entity.Stmt;
import com.sum.shy.library.StringUtils;

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

		Converter.register("fast_add", new FastAddConverter());// 快速添加

		Converter.register("return", new DefaultConverter());// 返回

	}

	public String build(CtClass clazz) {

		System.out.println();
		System.out.println("=================================== Java ========================================");

		// 构建java代码是倒过来的,这样能够在构建的时候,再重建一部分class信息
		String methods = buildMethods(clazz);
		String fields = buildFields(clazz);
		String classStr = buildClass(clazz, fields, methods);
		String head = buildHead(clazz);

		return head + classStr;

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
		String extendsStr = clazz.superName != null ? String.format("extends %s ", clazz.superName) : "";
		String implementsStr = clazz.interfaces.size() > 0
				? String.format("implements %s ", Joiner.on(", ").join(clazz.interfaces))
				: "";
		body.append(String.format("public class %s%s%s{\n\n", clazz.typeName + " ", extendsStr, implementsStr));
		body.append(fields);
		body.append(methods);
		body.append("}\n");// 追加一个class末尾
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
			body.append(buildField(clazz, "static", field));
		}
		for (CtField field : clazz.fields) {
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
			body.append(buildMethod(clazz, "static", method));
		}
		for (CtMethod method : clazz.methods) {
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
		if (StringUtils.isNotEmpty(desc)) {
			body.append(String.format("\tpublic %s %s %s(%s) {\n", desc, method.type, method.name, paramStr));
		} else {
			body.append(String.format("\tpublic %s %s(%s) {\n", method.type, method.name, paramStr));
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
				Converter converter = Converter.get(stmt.syntax);
				stmt = converter.convert(clazz, method, indent, block, line, stmt);
				body.append(indent + stmt + "\n");
				return null;// 必须返回null,才能够持续进行下去
			}
		});

	}

}

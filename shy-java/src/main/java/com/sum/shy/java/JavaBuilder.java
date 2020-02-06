package com.sum.shy.java;

import com.google.common.base.Joiner;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.CoopClass;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.api.Annotated;
import com.sum.shy.core.doc.Line;
import com.sum.shy.core.doc.Stmt;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.core.processor.MethodResolver;
import com.sum.shy.core.processor.api.Handler;
import com.sum.shy.java.api.Converter;
import com.sum.shy.java.convert.ConditionConverter;
import com.sum.shy.java.convert.FastAddConverter;
import com.sum.shy.java.convert.PrintConverter;
import com.sum.shy.java.convert.SimpleConverter;
import com.sum.shy.lib.StringUtils;

public class JavaBuilder {

	static {

		Converter.register(Constants.SUPER_SYNTAX, new SimpleConverter());
		Converter.register(Constants.THIS_SYNTAX, new SimpleConverter());

		Converter.register(Constants.DECLARE_SYNTAX, new SimpleConverter());
		Converter.register(Constants.ASSIGN_SYNTAX, new SimpleConverter());
		Converter.register(Constants.FIELD_ASSIGN_SYNTAX, new SimpleConverter());
		Converter.register(Constants.INVOKE_SYNTAX, new SimpleConverter());
		Converter.register(Constants.RETURN_SYNTAX, new SimpleConverter());

		Converter.register(Constants.IF_SYNTAX, new ConditionConverter());// --condition
		Converter.register(Constants.ELSEIF_SYNTAX, new ConditionConverter());// --condition
		Converter.register(Constants.ELSE_SYNTAX, new SimpleConverter());
		Converter.register(Constants.END_SYNTAX, new SimpleConverter());

		Converter.register(Constants.FOR_SYNTAX, new SimpleConverter());
		Converter.register(Constants.FOR_IN_SYNTAX, new SimpleConverter());

		Converter.register(Constants.WHILE_SYNTAX, new ConditionConverter());// --condition
		Converter.register(Constants.CONTINUE_SYNTAX, new SimpleConverter());
		Converter.register(Constants.BREAK_SYNTAX, new SimpleConverter());

		Converter.register(Constants.TRY_SYNTAX, new SimpleConverter());
		Converter.register(Constants.CATCH_SYNTAX, new SimpleConverter());
		Converter.register(Constants.FINALLY_SYNTAX, new SimpleConverter());
		Converter.register(Constants.THROW_SYNTAX, new SimpleConverter());

		Converter.register(Constants.SYNC_SYNTAX, new SimpleConverter());

		Converter.register(Constants.PRINT_SYNTAX, new PrintConverter());// --print
		Converter.register(Constants.DEBUG_SYNTAX, new PrintConverter());// --print
		Converter.register(Constants.ERROR_SYNTAX, new PrintConverter());// --print

		Converter.register(Constants.FAST_ADD_SYNTAX, new FastAddConverter());// --fast_add
		Converter.register(Constants.JUDGE_INVOKE_SYNTAX, new SimpleConverter());

	}

	public String build(IClass clazz) {

		System.out.println();
		System.out.println("=================================== Java ========================================");

		if ("interface".equals(clazz.category)) {
			String head = buildHead(clazz);
			String classStr = buildInterface(clazz);
			return head + classStr;

		} else {
			// 构建java代码是倒过来的,这样能够在构建的时候,再重建一部分class信息
			if (!(clazz instanceof CoopClass)) {
				String methods = buildMethods(clazz);
				String fields = buildFields(clazz);
				String classStr = buildClass(clazz, fields, methods);
				String head = buildHead(clazz);
				return head + classStr;
			} else {
				// 内部类不用加头部
				String methods = buildMethods(clazz);
				String fields = buildFields(clazz);
				String classStr = buildClass(clazz, fields, methods);
				return classStr;
			}

		}

	}

	/**
	 * 构建头部
	 * 
	 * @param clazz
	 * @return
	 */
	public String buildHead(IClass clazz) {

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

	public String buildInterface(IClass clazz) {
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
	public String buildClass(IClass clazz, String fields, String methods) {
		StringBuilder body = new StringBuilder();
		String desc = clazz instanceof CoopClass ? "static " : "";// 内部类需要是静态比较好
		String abstractStr = "abstract".equals(clazz.category) ? "abstract " : "";
		String extendsStr = clazz.superName != null ? String.format("extends %s ", clazz.superName) : "";
		String implementsStr = clazz.interfaces.size() > 0
				? String.format("implements %s ", Joiner.on(", ").join(clazz.interfaces))
				: "";
		body.append(buildAnnotations("", clazz));// 构建上面的注解
		body.append(String.format("public %s%sclass %s%s%s{\n\n", desc, abstractStr, clazz.typeName + " ", extendsStr,
				implementsStr));
		body.append(fields);
		body.append(methods);
		// 在这里把内部类拼上
		for (IClass coopClass : clazz.coopClasses.values()) {
			body.append("\t" + build(coopClass).replaceAll("\n", "\n\t"));
			// 删除最后一个缩进
			if (body.charAt(body.length() - 1) == '\t')
				body.deleteCharAt(body.length() - 1);
			body.append("\n");
		}
		body.append("}\n");// 追加一个class末尾
		return body.toString();
	}

	/**
	 * 构建注解
	 * 
	 * @param element
	 * @return
	 */
	public String buildAnnotations(String indent, Annotated annotated) {
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
	public String buildFields(IClass clazz) {

		StringBuilder body = new StringBuilder();

		for (IField field : clazz.staticFields) {
			body.append(buildAnnotations("\t", field));
			body.append(buildField(clazz, "static", field));
		}
		for (IField field : clazz.fields) {
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
	public String buildField(IClass clazz, String scope, IField field) {
		if (field.stmt != null) {
			if (StringUtils.isNotEmpty(scope)) {
				JavaConverter.convert(clazz, field.stmt);
				return String.format("\tpublic %s %s %s;\n", scope, field.type, field.stmt);
			} else {
				JavaConverter.convert(clazz, field.stmt);
				return String.format("\tpublic %s %s;\n", field.type, field.stmt);
			}
		} else {
			return String.format("\tpublic %s %s;\n", field.type, field.name);
		}

	}

	/**
	 * 构建方法体
	 * 
	 * @param clazz
	 * @return
	 */
	public String buildMethods(IClass clazz) {

		StringBuilder body = new StringBuilder();
		for (IMethod method : clazz.staticMethods) {
			body.append(buildAnnotations("\t", method));
			body.append(buildMethod(clazz, "static ", method));
		}
		for (IMethod method : clazz.methods) {
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
	public String buildMethod(IClass clazz, String desc, IMethod method) {
		StringBuilder body = new StringBuilder();
		String paramStr = "";

		if ("static ".equals(desc) && "main".equals(method.name)) {// 主方法自动加参数
			paramStr = "String[] args";
		} else if (method.params.size() > 0) {
			paramStr = Joiner.on(", ").join(method.params);
		}
		// 可能会抛出的异常
		String exceptions = method.exceptions.size() > 0
				? String.format("throws %s ", Joiner.on(", ").join(method.exceptions))
				: "";
		// 如果是构造函数,那就没有返回值了
		String returnType = method.name.equals(clazz.typeName) ? "" : method.type.toString() + " ";

		// public static synchronized void method(String param) throws Exception {
		body.append(String.format("\tpublic %s%s%s%s(%s) %s{\n", desc, method.isSync ? "synchronized " : "", returnType,
				method.name, paramStr, exceptions));

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
	public static void convertMethod(StringBuilder body, IClass clazz, IMethod method) {
//		MethodResolver.resolve(clazz, method, new Handler() {
//			@Override
//			public Object handle(IClass clazz, IMethod method, String indent, String block, Line line, Stmt stmt) {
//				try {
//					Converter converter = Converter.get(stmt.syntax);
//					stmt = converter.convert(clazz, method, indent, block, line, stmt);
//					body.append(indent + stmt + "\n");
//					return null;// 必须返回null,才能够持续进行下去
//				} catch (Exception e) {
//					System.out.println(stmt);
//					System.out.println(stmt.syntax);
//					throw e;
//				}
//			}
//		});

	}

}

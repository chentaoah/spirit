package com.sum.shy.java;

import com.sum.shy.clazz.IAnnotation;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.Import;
import com.sum.shy.common.Constants;
import com.sum.shy.element.Element;
import com.sum.shy.java.converter.StrLogicalConverter;
import com.sum.shy.linker.TypeBuilder;
import com.sum.shy.java.converter.SeparatorConverter;
import com.sum.shy.java.converter.StmtConverter;
import com.sum.shy.java.converter.StrEqualsConverter;
import com.sum.shy.java.converter.CommonConverter;

public class JavaBuilder {

	public String build(IClass clazz) {
		StringBuilder sb = new StringBuilder();
		String body = buildBody(clazz);// 这里构建body的原因是在构建时，还要动态添加import
		String head = buildHead(clazz);
		sb.append(head);
		sb.append(body);
		return sb.toString();
	}

	public String buildHead(IClass clazz) {
		StringBuilder sb = new StringBuilder();
		// 包名
		sb.append(String.format("package %s;\n\n", clazz.packageStr));
		// 引入
		for (Import iImport : clazz.imports) {
			if (!iImport.hasAlias())
				sb.append(iImport.element + ";\n");
		}
		if (clazz.imports.size() > 0)
			sb.append("\n");
		// 注解
		for (IAnnotation annotation : clazz.annotations)
			sb.append(annotation.token + "\n");
		return sb.toString();
	}

	public String buildBody(IClass clazz) {
		// 类名
		StringBuilder classStr = new StringBuilder();
		classStr.append("public " + clazz.root.insertAfter(Constants.ABSTRACT_KEYWORD, Constants.CLASS_KEYWORD)
				.replaceKeyword(Constants.IMPL_KEYWORD, "implements") + "\n\n");

		// 这里倒过来的原因是，在转换方法体时，需要根据需要动态添加字段
		StringBuilder methodsStr = new StringBuilder();
		for (IMethod method : clazz.methods) {// public static type + element
			Element element = method.element;
			for (IAnnotation annotation : method.annotations)
				methodsStr.append("\t" + annotation + "\n");
			// 如果是主方法，则使用java的主方法样式
			if (method.isStatic && "main".equals(method.name)) {
				methodsStr.append("\tpublic static void main(String[] args) {\n");
			} else {
				if (element.isFuncDeclare()) {
					String format = element.children.size() > 0 ? "\tpublic %s%s%s\n" : "\tpublic %s%s%s;\n\n";
					methodsStr.append(String.format(format, method.isStatic ? "static " : "",
							method.isSync ? "synchronized " : "", element.removeKeyword(Constants.SYNC_KEYWORD)));

				} else if (element.isFunc()) {
					String format = "\tpublic %s%s%s%s\n";
					methodsStr.append(String.format(format, method.isStatic ? "static " : "",
							method.isSync ? "synchronized " : "",
							!method.isInit ? TypeBuilder.build(clazz, method.type) + " " : "",
							element.removeKeyword(Constants.FUNC_KEYWORD).removeKeyword(Constants.SYNC_KEYWORD)));
				}

			}
			if (element.children.size() > 0) {// 构建方法体
				convertMethodElement(methodsStr, "\t\t", clazz, method.element);
				methodsStr.append("\t}\n\n");
			}
		}

		// 字段
		StringBuilder fieldsStr = new StringBuilder();
		for (IField field : clazz.fields) {// public static type + element
			for (IAnnotation annotation : field.annotations)
				fieldsStr.append("\t" + annotation + "\n");
			String format = "\tpublic %s%s\n";
			fieldsStr.append(String.format(format, field.isStatic ? "static " : "", convert(clazz, field.element)));
		}
		if (fieldsStr.length() > 0)
			fieldsStr.append("\n");

		classStr.append(fieldsStr).append(methodsStr).append("}\n");
		return classStr.toString();

	}

	public void convertMethodElement(StringBuilder sb, String indent, IClass clazz, Element father) {
		for (Element element : father.children) {
			sb.append(indent + convert(clazz, element) + "\n");
			if (element.children.size() > 0)
				convertMethodElement(sb, indent + "\t", clazz, element);
		}
	}

	public Element convert(IClass clazz, Element element) {
		// 1.基本转换，添加new关键字，将函数集合装换成方法构造
		CommonConverter.convertStmt(clazz, element.stmt);
		// 2.重载了字符串的==操作，和判空
		StrEqualsConverter.convertStmt(clazz, element.stmt);
		// 3.类型隐喻,逻辑符在操作字符串时，字符串转换为判空
		StrLogicalConverter.convertStmt(clazz, element.stmt);
		// 4.特殊语句的特殊处理
		StmtConverter.convert(clazz, element);
		// 5.添加括号和行结束符
		SeparatorConverter.convert(clazz, element);

		return element;
	}

}

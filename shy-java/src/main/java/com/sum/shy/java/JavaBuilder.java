package com.sum.shy.java;

import com.sum.shy.core.clazz.IAnnotation;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.Import;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.entity.Constants;

public class JavaBuilder {

	public String build(IClass clazz) {
		StringBuilder sb = new StringBuilder();
		buildClass(sb, clazz);
		return sb.toString();
	}

	public void buildClass(StringBuilder sb, IClass clazz) {
		// 包名
		sb.append(String.format("package %s;\n\n", clazz.packageStr));
		// 引入
		for (Import iImport : clazz.imports) {
			if (!iImport.hasAlias())
				sb.append(iImport.element + ";\n");
		}
		sb.append("\n");
		// 注解
		for (IAnnotation annotation : clazz.annotations)
			sb.append(annotation.token + "\n");
		// 类名
		sb.append(clazz.root.insertAfterKeyword(Constants.ABSTRACT_KEYWORD, Constants.CLASS_KEYWORD)
				.replaceKeyword(Constants.IMPL_KEYWORD, "implements") + "\n\n");
		// 字段
		for (IField field : clazz.fields) {// public static type + element
			String format = "public %s%s%s;\n\n";
			Element element = field.element;
			if (element.isDeclare() || element.isDeclareAssign()) {
				sb.append(String.format(format, field.isStatic ? "static " : "", "", field.element));
			} else if (element.isAssign()) {
				sb.append(String.format(format, field.isStatic ? "static " : "", field.type + " ", field.element));
			}
		}
		// 方法
		for (IMethod method : clazz.methods) {// public static type + element
			String format = "public %s%s%s%s\n";
			sb.append(String.format(format, method.isStatic ? "static " : "", method.isSync ? "synchronized " : "",
					!method.isInit ? method.type + " " : "",
					method.element.removeKeyword(Constants.FUNC_KEYWORD).removeKeyword(Constants.SYNC_KEYWORD)));
			buildMethod(sb, clazz, method);// 构建方法体
			sb.append("}\n\n");
		}
		sb.append("\n}\n");
	}

	public void buildMethod(StringBuilder sb, IClass clazz, IMethod method) {
		// 1.基本转换，添加new关键字，将函数集合装换成方法构造
		// 2.重载了字符串的==操作，和判空
		// 3.特殊语句的特殊处理，如日志打印
		// 4.替换某些关键字
		// 5.添加类型声明，赋值语法
		// 6.添加括号和行结束符

	}

}

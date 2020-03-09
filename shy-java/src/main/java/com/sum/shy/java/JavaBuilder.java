package com.sum.shy.java;

import com.sum.shy.core.clazz.IAnnotation;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.Import;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.entity.Constants;
import com.sum.shy.java.converter.KeywordConverter;
import com.sum.shy.java.converter.SeparatorConverter;
import com.sum.shy.java.converter.StmtConverter;
import com.sum.shy.java.converter.SymbolConverter;
import com.sum.shy.java.converter.TokenConverter;
import com.sum.shy.java.converter.TypeConverter;

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
			// 构建方法体
			convertElement(sb, "\t", clazz, method.element);
			sb.append("}\n\n");
		}
		sb.append("\n}\n");
	}

	public void convertElement(StringBuilder sb, String indent, IClass clazz, Element father) {
		for (Element element : father) {
			sb.append(indent + convert(clazz, element));
			if (element.size() > 0)
				convertElement(sb, indent + "\t", clazz, element);
		}
	}

	public Element convert(IClass clazz, Element element) {
		// 1.基本转换，添加new关键字，将函数集合装换成方法构造
		TokenConverter.convertStmt(clazz, element.stmt);
		// 2.重载了字符串的==操作，和判空
		SymbolConverter.convert(clazz, element);
		// 3.特殊语句的特殊处理，如日志打印
		StmtConverter.convert(clazz, element);
		// 4.添加括号和行结束符
		SeparatorConverter.convert(clazz, element);
		// 5.添加类型声明，赋值语法
		TypeConverter.convert(clazz, element);
		// 6.替换某些关键字
		KeywordConverter.convert(clazz, element);

		return element;
	}

}

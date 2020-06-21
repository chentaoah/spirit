package com.sum.shy.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.StaticFactory;
import com.sum.pisces.utils.AnnotationUtils;
import com.sum.shy.api.CodeBuilder;
import com.sum.shy.api.convert.ElementConverter;
import com.sum.shy.pojo.clazz.IAnnotation;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IField;
import com.sum.shy.pojo.clazz.IMethod;
import com.sum.shy.pojo.clazz.Import;
import com.sum.shy.pojo.common.Constants;
import com.sum.shy.pojo.element.Element;
import com.sum.shy.utils.TypeBuilder;

public class JavaBuilder implements CodeBuilder {

	public List<ElementConverter> converters = new ArrayList<>();

	public JavaBuilder() {
		Map<String, ElementConverter> converterMap = StaticFactory.FACTORY.getBeansOfType(ElementConverter.class);
		converters.addAll(converterMap.values());
		AnnotationUtils.sortByOrder(converters);
	}

	@Override
	public String build(IClass clazz) {
		StringBuilder builder = new StringBuilder();
		// When building a method, sometimes imports and fields is added
		// dynamically, so execute the method first
		String body = buildBody(clazz);
		String head = buildHead(clazz);
		return builder.append(head).append(body).toString();
	}

	public String buildHead(IClass clazz) {

		StringBuilder builder = new StringBuilder();
		// package
		builder.append(String.format("package %s;\n\n", clazz.packageStr));
		// import
		boolean flag = false;
		for (Import iImport : clazz.imports) {
			if (!iImport.hasAlias()) {
				flag = true;
				builder.append(iImport.element + ";\n");
			}
		}
		if (flag)
			builder.append("\n");
		// annotation
		for (IAnnotation annotation : clazz.annotations)
			builder.append(annotation + "\n");

		return builder.toString();
	}

	public String buildBody(IClass clazz) {

		StringBuilder classStr = new StringBuilder();
		classStr.append("public "
				+ clazz.root.insertAfter(Constants.ABSTRACT_KEYWORD, Constants.CLASS_KEYWORD).replaceKeyword(Constants.IMPLS_KEYWORD, "implements") + "\n\n");

		// When building a method, sometimes imports and fields is added
		// dynamically, so execute the method first
		StringBuilder methodsStr = new StringBuilder();

		// public static type + element
		for (IMethod method : clazz.methods) {
			Element element = method.element;

			// annotation
			for (IAnnotation annotation : method.annotations)
				methodsStr.append("\t" + annotation + "\n");

			// If this method is the main method
			if (method.isStatic && "main".equals(method.name)) {
				methodsStr.append("\tpublic static void main(String[] args) {\n");

			} else {
				// public User()
				// public static synchronized String methodName()
				if (element.isFuncDeclare()) {
					String format = element.hasChildElement() ? "\tpublic %s%s%s\n" : "\tpublic %s%s%s;\n\n";
					methodsStr.append(String.format(format, method.isStatic ? "static " : "", method.isSync ? "synchronized " : "",
							element.removeKeyword(Constants.SYNC_KEYWORD)));

				} else if (element.isFunc()) {
					String format = "\tpublic %s%s%s%s\n";
					methodsStr.append(String.format(format, method.isStatic ? "static " : "", method.isSync ? "synchronized " : "",
							!method.isInit ? TypeBuilder.build(clazz, method.type) + " " : "",
							element.removeKeyword(Constants.FUNC_KEYWORD).removeKeyword(Constants.SYNC_KEYWORD)));
				}
			}
			// Content building within methods
			if (element.hasChildElement()) {
				convertMethodElement(methodsStr, "\t\t", clazz, method.element);
				methodsStr.append("\t}\n\n");
			}
		}

		// fields
		StringBuilder fieldsStr = new StringBuilder();
		// public static type + element
		for (IField field : clazz.fields) {
			// annotation
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
			if (element.hasChildElement())
				convertMethodElement(sb, indent + "\t", clazz, element);
		}
	}

	public Element convert(IClass clazz, Element element) {
		for (ElementConverter converter : converters)
			converter.convert(clazz, element);
		return element;
	}

}

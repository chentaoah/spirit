package com.sum.jass.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sum.pisces.core.StaticFactory;
import com.sum.pisces.utils.AnnotationUtils;
import com.sum.jass.api.CodeBuilder;
import com.sum.jass.api.convert.AnnotationConverter;
import com.sum.jass.api.convert.ElementConverter;
import com.sum.jass.pojo.clazz.IAnnotation;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.clazz.IField;
import com.sum.jass.pojo.clazz.IMethod;
import com.sum.jass.pojo.clazz.Import;
import com.sum.jass.pojo.common.Constants;
import com.sum.jass.pojo.element.Element;

public class JavaBuilder implements CodeBuilder {

	public List<AnnotationConverter> annoConverters = new ArrayList<>();

	public List<ElementConverter> converters = new ArrayList<>();

	public JavaBuilder() {

		Map<String, AnnotationConverter> annoConverterMap = StaticFactory.FACTORY.getBeansOfType(AnnotationConverter.class);
		annoConverters.addAll(annoConverterMap.values());
		AnnotationUtils.sortByOrder(annoConverters);

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
		for (IAnnotation annotation : clazz.annotations) {
			annotation = convert(clazz, annotation);
			if (annotation != null)
				builder.append(annotation + "\n");
		}

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
			for (IAnnotation annotation : method.annotations) {
				annotation = convert(clazz, annotation);
				if (annotation != null)
					methodsStr.append("\t" + annotation + "\n");
			}

			// If this method is the main method
			if (method.isStatic && "main".equals(method.name)) {
				methodsStr.append("\tpublic static void main(String[] args) {\n");

			} else {
				// public User()
				// public static synchronized String methodName()
				if (element.isFuncDeclare()) {
					String format = element.hasChildElement() ? "\tpublic %s%s%s%s\n" : "\tpublic %s%s%s%s;\n\n";
					String staticDesc = method.isStatic ? "static " : "";
					String abstractDesc = clazz.isAbstract() && !element.hasChildElement() ? "abstract " : "";
					String syncDesc = method.isSync ? "synchronized " : "";
					methodsStr.append(String.format(format, staticDesc, abstractDesc, syncDesc, element.removeKeyword(Constants.SYNC_KEYWORD)));

				} else if (element.isFunc()) {
					String format = "\tpublic %s%s%s%s\n";
					String staticDesc = method.isStatic ? "static " : "";
					String syncDesc = method.isSync ? "synchronized " : "";
					methodsStr.append(String.format(format, staticDesc, syncDesc, !method.isInit ? TypeBuilder.build(clazz, method.type) + " " : "",
							element.removeKeyword(Constants.FUNC_KEYWORD).removeKeyword(Constants.SYNC_KEYWORD)));
				}
			}
			// Content building within methods
			// Note that this may be an empty element!!!
			if (element.hasChild() || element.hasChildElement()) {
				convertMethodElement(methodsStr, "\t\t", clazz, method.element);
				methodsStr.append("\t}\n\n");
			}
		}

		// fields
		StringBuilder fieldsStr = new StringBuilder();
		// public static type + element
		for (IField field : clazz.fields) {

			// annotation
			for (IAnnotation annotation : field.annotations) {
				annotation = convert(clazz, annotation);
				if (annotation != null)
					fieldsStr.append("\t" + annotation + "\n");
			}

			String format = "\tpublic %s%s\n";
			fieldsStr.append(String.format(format, field.isStatic ? "static " : "", convert(clazz, field.element)));
		}
		if (fieldsStr.length() > 0)
			fieldsStr.append("\n");

		classStr.append(fieldsStr).append(methodsStr).append("}\n");
		return classStr.toString();

	}

	public void convertMethodElement(StringBuilder builder, String indent, IClass clazz, Element father) {
		for (Element element : father.children) {
			builder.append(indent + convert(clazz, element) + "\n");
			if (element.hasChildElement())
				convertMethodElement(builder, indent + "\t", clazz, element);
		}
	}

	public IAnnotation convert(IClass clazz, IAnnotation annotation) {
		for (AnnotationConverter converter : annoConverters) {
			if (converter.isMatch(clazz, annotation))
				return converter.convert(clazz, annotation);
		}
		return annotation;
	}

	public Element convert(IClass clazz, Element element) {
		for (ElementConverter converter : converters)
			converter.convert(clazz, element);
		return element;
	}

}

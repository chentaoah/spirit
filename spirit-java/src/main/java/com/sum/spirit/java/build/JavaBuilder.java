package com.sum.spirit.java.build;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.Import;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class JavaBuilder implements CodeBuilder, InitializingBean {

	public static final String IMPLEMENTS_KEYWORD = "implements";
	public static final String FINAL_KEYWORD = "final";
	public static final String SYNCHRONIZED_KEYWORD = "synchronized";

	public List<ElementConverter> converters;

	@Override
	public void afterPropertiesSet() throws Exception {
		converters = SpringUtils.getBeansAndSort(ElementConverter.class);
	}

	@Override
	public String build(IClass clazz) {
		String body = buildBody(clazz);
		String head = buildHead(clazz);
		return new StringBuilder().append(head).append(body).toString();
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
		classStr.append(
				clazz.element.insertStatement(KeywordEnum.ABSTRACT.value, KeywordEnum.CLASS.value).replaceStatement(KeywordEnum.IMPLS.value, IMPLEMENTS_KEYWORD)
						+ "\n\n");

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
			if (method.isStatic() && "main".equals(method.name)) {
				methodsStr.append("\tpublic static void main(String[] args) {\n");

			} else {
				// public User()
				// public static synchronized String methodName()
				element.replaceModifier(KeywordEnum.SYNCH.value, SYNCHRONIZED_KEYWORD);
				if (element.isFuncDeclare()) {
					if (clazz.isAbstract() && !method.isStatic() && !element.hasChild())
						element.insertModifier(KeywordEnum.PUBLIC.value, KeywordEnum.ABSTRACT.value);
					if (element.hasChild()) {
						methodsStr.append("\t" + element + "\n");
					} else {
						methodsStr.append("\t" + element + ";\n\n");
					}

				} else if (element.isFunc()) {
					if (method.isInit) {
						element.removeKeyword(KeywordEnum.FUNC.value);
					} else {
						element.replaceKeyword(KeywordEnum.FUNC.value, TypeBuilder.build(clazz, method.type));
					}
					methodsStr.append("\t" + element + "\n");
				}
			}
			// Content building within methods
			// Note that this may be an empty element!!!
			if (element.hasChild()) {
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

			field.element.replaceModifier(KeywordEnum.CONST.value, FINAL_KEYWORD);
			fieldsStr.append("\t" + convert(clazz, field.element) + "\n");
		}
		if (fieldsStr.length() > 0)
			fieldsStr.append("\n");

		classStr.append(fieldsStr).append(methodsStr).append("}\n");
		return classStr.toString();

	}

	public void convertMethodElement(StringBuilder builder, String indent, IClass clazz, Element father) {
		for (Element element : father.children) {
			builder.append(indent + convert(clazz, element) + "\n");
			if (element.hasChild())
				convertMethodElement(builder, indent + "\t", clazz, element);
		}
	}

	public Element convert(IClass clazz, Element element) {
		for (ElementConverter converter : converters)
			converter.convert(clazz, element);
		return element;
	}

}

package com.sum.spirit.java.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.CodeBuilder;
import com.sum.spirit.java.api.ElementConverter;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.Import;
import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.utils.SpringUtils;

@Component
@DependsOn("springUtils")
public class JavaBuilder implements CodeBuilder {

	public List<ElementConverter> converters;

	public JavaBuilder() {
		Map<String, ElementConverter> converterMap = SpringUtils.getBeansOfType(ElementConverter.class);
		converters = new ArrayList<>(converterMap.values());
		converters.sort(new AnnotationAwareOrderComparator());
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
		classStr.append(clazz.element.insertStatement(Constants.ABSTRACT_KEYWORD, Constants.CLASS_KEYWORD).replaceStatement(Constants.IMPLS_KEYWORD,
				Constants.IMPLEMENTS_KEYWORD) + "\n\n");

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
				element.replaceModifier(Constants.SYNCH_KEYWORD, Constants.SYNCHRONIZED_KEYWORD);
				if (element.isFuncDeclare()) {
					if (clazz.isAbstract() && !method.isStatic() && !element.hasChild())
						element.insertModifier(Constants.PUBLIC_KEYWORD, Constants.ABSTRACT_KEYWORD);
					if (element.hasChild()) {
						methodsStr.append("\t" + element + "\n");
					} else {
						methodsStr.append("\t" + element + ";\n\n");
					}

				} else if (element.isFunc()) {
					if (method.isInit) {
						element.removeKeyword(Constants.FUNC_KEYWORD);
					} else {
						element.replaceKeyword(Constants.FUNC_KEYWORD, TypeBuilder.build(clazz, method.type));
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

			field.element.replaceModifier(Constants.CONST_KEYWORD, Constants.FINAL_KEYWORD);
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

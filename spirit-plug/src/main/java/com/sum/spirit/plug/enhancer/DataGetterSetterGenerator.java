package com.sum.spirit.plug.enhancer;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassEnhancer;
import com.sum.spirit.core.MemberVisiter;
import com.sum.spirit.core.lexer.ElementBuilder;
import com.sum.spirit.plug.annotation.Data;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.enums.KeywordEnum;

@Component
public class DataGetterSetterGenerator implements ClassEnhancer {

	@Autowired
	public ElementBuilder builder;
	@Autowired
	public MemberVisiter visiter;

	@Override
	public void enhance(IClass clazz) {
		IAnnotation annotation = clazz.getAnnotation(Data.class.getName());
		if (annotation != null) {
			for (IField field : clazz.fields) {
				addGetMethod(clazz, field);
				addSetMethod(clazz, field);
			}
		}
	}

	public void addGetMethod(IClass clazz, IField field) {
		String name = toUpperCaseAtFirst(field.getName());
		Element element = builder.build(String.format("func get%s() {", name)).addModifier(KeywordEnum.PUBLIC.value);
		Element content = builder.build(String.format("\treturn %s", field.getName()));
		element.children.add(content);
		IMethod method = new IMethod(new ArrayList<>(), element);
		visiter.visitParameters(clazz, method);
		visiter.visitMember(clazz, method);
		clazz.methods.add(method);
	}

	public void addSetMethod(IClass clazz, IField field) {
		String name = toUpperCaseAtFirst(field.getName());
		Element element = builder.build(String.format("func set%s(%s %s) {", name, field.getType().getSimpleName(), field.getName()))
				.addModifier(KeywordEnum.PUBLIC.value);
		Element content = builder.build(String.format("\tthis.%s = %s", field.getName(), field.getName()));
		element.children.add(content);
		IMethod method = new IMethod(new ArrayList<>(), element);
		visiter.visitParameters(clazz, method);
		visiter.visitMember(clazz, method);
		clazz.methods.add(method);
	}

	public String toUpperCaseAtFirst(String name) {
		String first = name.substring(0, 1);
		String other = name.substring(1);
		return first.toUpperCase() + other;
	}

}

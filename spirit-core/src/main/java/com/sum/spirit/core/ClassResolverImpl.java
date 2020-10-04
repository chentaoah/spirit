package com.sum.spirit.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.api.ClassResolver;
import com.sum.spirit.api.lexer.ElementBuilder;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.Import;
import com.sum.spirit.pojo.common.KeywordEnum;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.utils.TypeUtils;

@Component
public class ClassResolverImpl implements ClassResolver {

	@Autowired
	public ElementBuilder builder;

	@Override
	public List<IClass> resolve(String packageStr, Document document) {

		List<IClass> classes = new ArrayList<>();
		IClass mainClass = new IClass();
		classes.add(mainClass);
		mainClass.packageStr = packageStr;

		// Temporary variables, need to be cleared after use
		List<IAnnotation> annotations = new ArrayList<>();

		for (Element element : document) {
			if (element.isImport()) {
				mainClass.imports.add(new Import(element));

			} else if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				element.addModifier(KeywordEnum.STATIC.value).addModifier(KeywordEnum.PUBLIC.value);
				mainClass.fields.add(new IField(annotations, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				element.addModifier(KeywordEnum.STATIC.value).addModifier(KeywordEnum.PUBLIC.value);
				mainClass.methods.add(new IMethod(annotations, element));
				annotations.clear();

			} else if (element.isInterface() || element.isAbstract()) {
				// Interface and abstract class can only have one main class
				mainClass.annotations.addAll(annotations);
				annotations.clear();
				mainClass.element = element.addModifier(KeywordEnum.PUBLIC.value);
				readRootElement(mainClass);

			} else if (element.isClass()) {
				// The generic type may be obtained here,
				// but the filename is usually a simple name
				String simpleName = element.getKeywordParam(KeywordEnum.CLASS.value).toString();
				String targetName = TypeUtils.getTargetName(simpleName);

				if (document.name.equals(targetName)) {
					mainClass.annotations.addAll(annotations);
					annotations.clear();
					mainClass.element = element.addModifier(KeywordEnum.PUBLIC.value);
					readRootElement(mainClass);

				} else {
					IClass clazz = new IClass();
					clazz.packageStr = packageStr;
					clazz.imports = mainClass.imports;// 共用！
					clazz.annotations.addAll(annotations);
					annotations.clear();
					clazz.element = element.addModifier(KeywordEnum.PUBLIC.value);
					readRootElement(clazz);
					classes.add(clazz); // 添加协同的类
				}
			}
		}

		// 如果不存在主类的声明，则虚拟一个Element
		if (mainClass.element == null)
			mainClass.element = builder.build("class " + document.name + " {").addModifier(KeywordEnum.PUBLIC.value);

		return classes;
	}

	public void readRootElement(IClass clazz) {

		// Temporary variables, need to be cleared after use
		List<IAnnotation> annotations = new ArrayList<>();

		for (Element element : clazz.element.children) {
			if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				clazz.fields.add(new IField(annotations, element.addModifier(KeywordEnum.PUBLIC.value)));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				clazz.methods.add(new IMethod(annotations, element.addModifier(KeywordEnum.PUBLIC.value)));
				annotations.clear();
			}
		}
	}

}

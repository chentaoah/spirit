package com.sum.spirit.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.core.build.ElementBuilder;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IField;
import com.sum.spirit.pojo.clazz.IMethod;
import com.sum.spirit.pojo.clazz.Import;
import com.sum.spirit.pojo.element.Document;
import com.sum.spirit.pojo.element.Element;
import com.sum.spirit.pojo.enums.KeywordEnum;
import com.sum.spirit.utils.TypeUtils;

@Component
public class ClassResolver {

	@Autowired
	public ElementBuilder builder;

	public List<IClass> resolve(String packageStr, Document document) {

		List<IClass> classes = new ArrayList<>();
		List<Import> imports = new ArrayList<>();
		List<IAnnotation> annotations = new ArrayList<>();
		IClass mainClass = null;
		List<IField> fields = new ArrayList<>();
		List<IMethod> methods = new ArrayList<>();

		for (Element element : document) {
			if (element.isImport()) {
				imports.add(new Import(element));

			} else if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element.getToken(0)));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				element.addModifier(KeywordEnum.STATIC.value).addModifier(KeywordEnum.PUBLIC.value);
				fields.add(new IField(annotations, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				element.addModifier(KeywordEnum.STATIC.value).addModifier(KeywordEnum.PUBLIC.value);
				methods.add(new IMethod(annotations, element));
				annotations.clear();

			} else if (element.isInterface() || element.isAbstract()) {
				// 接口和抽象类，只允许出现一个主类
				mainClass = new IClass(imports, annotations, element.addModifier(KeywordEnum.PUBLIC.value));
				annotations.clear();
				mainClass.packageStr = packageStr;
				mainClass.fields = fields;
				mainClass.methods = methods;
				readRootElement(mainClass);
				classes.add(mainClass);

			} else if (element.isClass()) {
				// 这里可能出现泛型，但是文件名一般是simpleName
				String simpleName = element.getKeywordParam(KeywordEnum.CLASS.value).toString();
				String targetName = TypeUtils.getTargetName(simpleName);

				if (document.name.equals(targetName)) {
					mainClass = new IClass(imports, annotations, element.addModifier(KeywordEnum.PUBLIC.value));
					annotations.clear();
					mainClass.packageStr = packageStr;
					mainClass.fields = fields;
					mainClass.methods = methods;
					readRootElement(mainClass);
					classes.add(mainClass);

				} else {
					IClass clazz = new IClass(imports, annotations, element.addModifier(KeywordEnum.PUBLIC.value));
					annotations.clear();
					clazz.packageStr = packageStr;
					clazz.fields = new ArrayList<>();
					clazz.methods = new ArrayList<>();
					readRootElement(clazz);
					classes.add(clazz);
				}
			}
		}

		// 如果不存在主类的声明，则虚拟一个Element
		if (mainClass == null) {
			Element element = builder.build("class " + document.name + " {");
			mainClass = new IClass(imports, annotations, element.addModifier(KeywordEnum.PUBLIC.value));
			annotations.clear();
			mainClass.packageStr = packageStr;
			mainClass.fields = fields;
			mainClass.methods = methods;
			classes.add(mainClass);
		}

		return classes;
	}

	public void readRootElement(IClass clazz) {

		List<IAnnotation> annotations = new ArrayList<>();

		for (Element element : clazz.element.children) {
			if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element.getToken(0)));

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

package com.sum.shy.api.service;

import java.util.ArrayList;
import java.util.List;

import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.ClassResolver;
import com.sum.shy.api.ElementBuilder;
import com.sum.shy.clazz.IAnnotation;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IField;
import com.sum.shy.clazz.IMethod;
import com.sum.shy.clazz.Import;
import com.sum.shy.common.Constants;
import com.sum.shy.element.Document;
import com.sum.shy.element.Element;
import com.sum.shy.utils.TypeUtils;

public class ClassResolverImpl implements ClassResolver {

	public ElementBuilder builder = ProxyFactory.get(ElementBuilder.class);

	@Override
	public List<IClass> resolverClasses(String packageStr, Document document) {

		List<IClass> classes = new ArrayList<>();
		IClass mainClass = new IClass();
		classes.add(mainClass);
		mainClass.packageStr = packageStr;
		List<IAnnotation> annotations = new ArrayList<>();// 上下文注解,用完要及时清理

		for (Element element : document) {
			if (element.isImport()) {
				mainClass.imports.add(new Import(element));

			} else if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				mainClass.fields.add(new IField(annotations, true, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				mainClass.methods.add(new IMethod(annotations, true, element));
				annotations.clear();

			} else if (element.isInterface() || element.isAbstract()) {
				mainClass.annotations.addAll(annotations);
				annotations.clear();
				mainClass.root = element;
				readRootElement(mainClass);

			} else if (element.isClass()) {
				String simpleName = element.getKeywordParam(Constants.CLASS_KEYWORD).toString();
				String targetName = TypeUtils.getTargetName(simpleName);

				if (document.name.equals(targetName)) {
					mainClass.annotations.addAll(annotations);
					annotations.clear();
					mainClass.root = element;
					readRootElement(mainClass);

				} else {
					IClass clazz = new IClass();
					clazz.packageStr = packageStr;
					clazz.imports = mainClass.imports;// 共用！
					clazz.annotations.addAll(annotations);
					annotations.clear();
					clazz.root = element;
					readRootElement(clazz);
					classes.add(clazz); // 添加协同的类
				}
			}
		}
		// 如果不存在主类的声明，则虚拟一个Element
		if (mainClass.root == null)
			mainClass.root = builder.build("class " + document.name + " {");

		return classes;
	}

	public void readRootElement(IClass clazz) {
		List<IAnnotation> annotations = new ArrayList<>();
		for (Element element : clazz.root.children) {
			if (element.isAnnotation()) {
				annotations.add(new IAnnotation(element));

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				clazz.fields.add(new IField(annotations, false, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				clazz.methods.add(new IMethod(annotations, false, element));
				annotations.clear();
			}
		}
	}

}

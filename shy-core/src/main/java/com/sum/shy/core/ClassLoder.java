package com.sum.shy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.IAnnotation;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.clazz.Import;
import com.sum.shy.core.document.Document;
import com.sum.shy.core.document.Element;
import com.sum.shy.core.entity.Constants;

public class ClassLoder {

	public List<IClass> load(String packageStr, File file) {
		// 1.生成docment对象
		Document document = new DocumentReader().read(file);
		// 2.打印日志
		document.debug();
		// 3.生成Class对象
		List<IClass> classes = load(packageStr, document);

		return classes;
	}

	public List<IClass> load(String packageStr, Document document) {

		List<IClass> classes = new ArrayList<>();
		// 主类
		IClass mainClass = new IClass();
		// 添加到集合中
		classes.add(mainClass);
		// 包名
		mainClass.packageStr = packageStr;
		// 上下文注解,用完要及时清理
		List<IAnnotation> annotations = new ArrayList<>();

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
				if (document.name.equals(element.getKeywordParam(Constants.CLASS_KEYWORD))) {
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
			mainClass.root = new Element("class " + document.name + " {");

		return classes;
	}

	public void readRootElement(IClass clazz) {
		List<IAnnotation> annotations = new ArrayList<>();
		for (Element element : clazz.root) {
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

package com.sum.shy.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.CoopClass;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;

public class ClassReader {

	public IClass read(File file) {
		// 1.生成docment对象
		Document document = new DocumentReader().read(file);
		// 2.打印日志
		document.debug();
		// 3.生成Class对象
		IClass clazz = read(document);
		// 4.打印日志
		clazz.debug();

		return clazz;
	}

	public IClass read(Document document) {
		// 主类
		IClass mainClass = new IClass();
		// 文档
		mainClass.document = document;
		// 上下文注解,用完要及时清理
		List<Element> annotations = new ArrayList<>();

		for (Element element : document) {
			if (element.isImport()) {
				mainClass.imports.add(element);

			} else if (element.isAnnotation()) {
				annotations.add(element);

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				mainClass.fields.add(new IField(annotations, true, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				mainClass.methods.add(new IMethod(mainClass, annotations, true, element));
				annotations.clear();

			} else if (element.isInterface()) {
				mainClass.annotations.addAll(annotations);
				annotations.clear();
				mainClass.root = element;
				readRootElement(mainClass);

			} else if (element.isAbstract()) {
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

				} else {// 协同内部类
					IClass coopClass = new CoopClass(mainClass);
					coopClass.annotations.addAll(annotations);
					annotations.clear();
					coopClass.root = element;
					readRootElement(coopClass);
					// 添加到主类中
					mainClass.coopClasses.add(coopClass);

				}

			}

		}
		return mainClass;
	}

	public void readRootElement(IClass clazz) {
		List<Element> annotations = new ArrayList<>();
		for (Element element : clazz.root) {
			if (element.isAnnotation()) {
				annotations.add(element);

			} else if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				clazz.fields.add(new IField(annotations, false, element));
				annotations.clear();

			} else if (element.isFuncDeclare() || element.isFunc()) {
				clazz.methods.add(new IMethod(clazz, annotations, false, element));
				annotations.clear();

			}

		}

	}

}

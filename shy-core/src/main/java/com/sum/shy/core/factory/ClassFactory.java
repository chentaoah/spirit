package com.sum.shy.core.factory;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.clazz.CoopClass;
import com.sum.shy.core.clazz.IClass;
import com.sum.shy.core.clazz.IField;
import com.sum.shy.core.clazz.IMethod;
import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;

public class ClassFactory {

	public static IClass create(Document document) {
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
				mainClass.methods.add(new IMethod(annotations, true, element));
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

	private static void readRootElement(IClass clazz) {
		List<Element> annotations = new ArrayList<>();
		for (Element element : clazz.root) {
			if (element.isAnnotation()) {
				annotations.add(element);

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

package com.sum.shy.core.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.shy.core.doc.Document;
import com.sum.shy.core.doc.Element;
import com.sum.shy.core.entity.Constants;

public class IClass {

	public Document document;

	public String packageStr;

	public List<Element> imports;

	public List<Element> annotations;

	public Element root;

	public List<IField> fields = new ArrayList<>();

	public List<IMethod> methods = new ArrayList<>();

	public List<IClass> coopClasses = new ArrayList<>();

	public IClass(Document document) {
		this.document = document;
		init(document);
	}

	private void init(Document document) {
		// 1.解析基本结构
		initRootElement(document);
		// 2.解析成员
		initMemberElements(document, root);
		// 3.解析内部类
		initCoopClasses(document);
	}

	private void initRootElement(Document document) {
		List<Element> annotations = new ArrayList<>();
		for (Element element : document) {
			if (element.isAnnotation()) {
				annotations.add(element);
				continue;
			}
			if (Constants.INTERFACE_SYNTAX.equals(element.syntax)) {
				this.annotations = annotations;
				this.root = element;
				break;

			} else if (Constants.ABSTRACT_SYNTAX.equals(element.syntax)) {
				this.annotations = annotations;
				this.root = element;
				break;

			} else if (Constants.CLASS_SYNTAX.equals(element.syntax)) {
				if (document.name.equals(element.getKeywordParam(Constants.CLASS_KEYWORD))) {
					this.annotations = annotations;
					this.root = element;
					break;

				}
			}
		}
		throw new RuntimeException("Unable to get class information!");
	}

	private void initMemberElements(Document document, Element root) {
		List<Element> annotations = new ArrayList<>();
		for (Element element : document) {
			if (element.isAnnotation()) {
				annotations.add(element);
				continue;
			}
			if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				fields.add(new IField(annotations, true, element));
				annotations.clear();

			} else if (element.isFunc()) {
				methods.add(new IMethod(annotations, true, element));
				annotations.clear();

			}
		}
		for (Element element : root) {
			if (element.isAnnotation()) {
				annotations.add(element);
				continue;
			}
			if (element.isDeclare() || element.isDeclareAssign() || element.isAssign()) {
				fields.add(new IField(annotations, false, element));
				annotations.clear();

			} else if (element.isFunc()) {
				methods.add(new IMethod(annotations, false, element));
				annotations.clear();

			}
		}
	}

	private void initCoopClasses(Document document) {
		// TODO Auto-generated method stub

	}

	public String findImport(String simpleName) {
		return null;
	}

	public boolean isInterface() {// 接口里不允许嵌套别的东西
		return Constants.INTERFACE_SYNTAX.equals(root.syntax);
	}

	public boolean isAbstract() {// 抽象类里也不允许嵌套
		return Constants.ABSTRACT_SYNTAX.equals(root.syntax);
	}

	public boolean isClass() {
		return Constants.CLASS_SYNTAX.equals(root.syntax);
	}

	public String getTypeName() {
		return document.name;
	}

	public String getClassName() {
		return packageStr + "." + getTypeName();
	}

	public String getSuperName() {
		return root.getKeywordParam(Constants.EXTENDS_KEYWORD);
	}

	public List<String> getInterfaces() {
		return root.getKeywordParams(Constants.IMPL_KEYWORD);
	}

}

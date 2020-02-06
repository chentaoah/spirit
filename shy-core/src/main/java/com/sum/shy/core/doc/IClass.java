package com.sum.shy.core.doc;

import com.sum.shy.core.entity.Constants;

public class IClass {

	public String packageStr;

	public Document document;

	public IClass(Document document) {
		this.document = document;
		init(document);

	}

	private void init(Document document) {
		// 1.解析基本结构

		// 2.变量追踪

		// 3.访问推导

	}

	public String findImport(String simpleName) {
		return null;
	}

	public boolean isInterface() {// 接口里不允许嵌套别的东西
		return document.findElement(Constants.INTERFACE_SYNTAX) != null;
	}

	public boolean isAbstract() {// 抽象类里也不允许嵌套
		return document.findElement(Constants.ABSTRACT_SYNTAX) != null;
	}

	public boolean isClass() {
		if (isInterface() || isAbstract())
			return false;
		// 文件中要求必须有主类
		Element element = document.findElement(Constants.CLASS_SYNTAX, Constants.CLASS_KEYWORD, document.name);
		if (element == null)
			throw new RuntimeException("The document must contain the main class!");
		return element != null;
	}

}

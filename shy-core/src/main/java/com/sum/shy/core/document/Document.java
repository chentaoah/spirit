package com.sum.shy.core.document;

import java.io.File;
import java.util.ArrayList;

import com.sum.shy.core.utils.TypeUtils;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String name;

	public Document(File file) {
		this.name = TypeUtils.getNameByFile(file);
	}

	public void debug() {
		for (Element element : this)
			element.debug();
	}

	public Element findElement(String syntax) {
		for (Element element : this) {
			if (syntax.equals(element.syntax))
				return element;
		}
		return null;
	}

	public Element findElement(String syntax, String keyword, String keywordParam) {
		for (Element element : this) {
			if (syntax.equals(element.syntax)) {
				if (keywordParam.equals(element.getKeywordParam(keyword)))
					return element;
			}
		}
		return null;
	}

}

package com.sum.shy.document.pojo;

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

}

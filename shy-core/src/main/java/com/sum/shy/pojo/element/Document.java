package com.sum.shy.pojo.element;

import java.io.File;
import java.util.ArrayList;

import com.sum.shy.utils.FileUtils;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String name;

	public Document(File file) {
		this.name = FileUtils.getName(file);
	}

	public void debug() {
		for (Element element : this)
			element.debug();
	}

}

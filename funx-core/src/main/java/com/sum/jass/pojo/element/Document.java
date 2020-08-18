package com.sum.jass.pojo.element;

import java.io.File;
import java.util.ArrayList;

import com.sum.jass.utils.FileUtils;

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

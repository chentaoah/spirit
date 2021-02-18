package com.sum.spirit.core.element.entity;

import java.io.File;
import java.util.ArrayList;

import com.sum.spirit.common.utils.ConfigUtils;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String fileName;

	public Document(File file) {
		this.fileName = file.getName().replace("." + ConfigUtils.getFileExtension(), "");
	}

	public Document(String fileName) {
		this.fileName = fileName;
	}

	public void debug() {
		for (Element element : this) {
			element.debug();
		}
	}

}

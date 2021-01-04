package com.sum.spirit.pojo.element.impl;

import java.io.File;
import java.util.ArrayList;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.ConfigUtils;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String fileName;

	public Document(File file) {
		String suffix = "."
				+ ConfigUtils.getProperty(Constants.FILENAME_EXTENSION_KEY, Constants.DEFAULT_FILENAME_EXTENSION);
		this.fileName = file.getName().replace(suffix, "");
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

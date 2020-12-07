package com.sum.spirit.pojo.element;

import java.io.File;
import java.util.ArrayList;

import com.sum.spirit.pojo.common.Constants;
import com.sum.spirit.utils.ConfigUtils;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String name;

	public Document(File file) {
		String suffix = "." + ConfigUtils.getProperty(Constants.FILE_SUFFIX_KEY);
		this.name = file.getName().replace(suffix, "");
	}

	public void debug() {
		for (Element element : this) {
			element.debug();
		}
	}

}

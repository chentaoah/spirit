package com.sum.shy.core.doc;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public Document(File file) {

	}

	public void debug() {
		for (Element element : this)
			element.debug();
	}

}

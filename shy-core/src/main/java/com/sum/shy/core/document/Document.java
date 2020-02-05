package com.sum.shy.core.document;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public Document(File file) {

	}

	public void show() {
		for (Element element : this)
			element.show();
	}

}

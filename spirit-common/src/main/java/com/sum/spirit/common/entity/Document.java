package com.sum.spirit.common.entity;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class Document extends ArrayList<Element> {

	public String fileName;

	public void debug() {
		for (Element element : this) {
			element.debug();
		}
	}

}

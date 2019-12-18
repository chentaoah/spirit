package com.sum.shy.core.clazz.api;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsAnnotated implements Annotated {

	public List<String> annotations = new ArrayList<>();

	public List<String> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<String> annotations) {
		this.annotations = annotations != null ? annotations : new ArrayList<>();
	}

}

package com.sum.shy.core.clazz.api;

import java.util.List;

public abstract class AbsAnnotated implements Annotated {

	public List<String> annotations;

	@Override
	public List<String> getAnnotations() {
		return annotations;
	}

}

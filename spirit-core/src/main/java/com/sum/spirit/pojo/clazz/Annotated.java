package com.sum.spirit.pojo.clazz;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.pojo.element.Element;

public abstract class Annotated extends Elemented {

	public List<IAnnotation> annotations;

	public Annotated(List<IAnnotation> annotations, Element element) {
		super(element);
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
	}

	public IAnnotation getAnnotation(String className) {
		for (IAnnotation annotation : annotations) {
			if (annotation.getType().getClassName().equals(className))
				return annotation;
		}
		return null;
	}

}
package com.sum.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.entity.Element;
import com.sum.spirit.core.clazz.entity.IAnnotation;

public abstract class AnnotationUnit extends ElementUnit {

	public List<IAnnotation> annotations;

	public AnnotationUnit(List<IAnnotation> annotations, Element element) {
		super(element);
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
	}

	public IAnnotation getAnnotation(String className) {
		for (IAnnotation annotation : annotations) {
			if (annotation.getType().getClassName().equals(className)) {
				return annotation;
			}
		}
		return null;
	}

	public IAnnotation removeAnnotation(String className) {
		IAnnotation annotation = getAnnotation(className);
		if (annotation != null) {
			annotations.remove(annotation);
			return annotation;
		}
		return null;
	}

}

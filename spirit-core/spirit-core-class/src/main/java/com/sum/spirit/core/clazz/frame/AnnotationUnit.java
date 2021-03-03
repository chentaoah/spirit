package com.sum.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;

import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.element.entity.Element;

public abstract class AnnotationUnit extends ElementUnit {

	public List<IAnnotation> annotations;

	public AnnotationUnit(List<IAnnotation> annotations, Element element) {
		super(element);
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
	}

	public IAnnotation getAnnotation(String className) {
		return Lists.findOne(annotations, annotation -> annotation.getType().getClassName().equals(className));
	}

	public IAnnotation removeAnnotation(String className) {
		return Lists.remove(annotations, annotation -> annotation.getType().getClassName().equals(className));
	}

}

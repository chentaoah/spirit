package com.gitee.spirit.core.clazz.frame;

import java.util.ArrayList;
import java.util.List;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.clazz.entity.IAnnotation;
import com.gitee.spirit.core.element.entity.Element;

public abstract class AnnotationEntity extends ElementEntity {

	public List<IAnnotation> annotations;

	public AnnotationEntity(List<IAnnotation> annotations, Element element) {
		super(element);
		this.annotations = annotations != null ? new ArrayList<>(annotations) : new ArrayList<>();
	}

	public IAnnotation getAnnotation(String className) {
		return ListUtils.findOne(annotations, annotation -> annotation.getType().getClassName().equals(className));
	}

	public IAnnotation removeAnnotation(String className) {
		return ListUtils.remove(annotations, annotation -> annotation.getType().getClassName().equals(className));
	}

}

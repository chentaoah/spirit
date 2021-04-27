package com.sum.spirit.core.clazz.frame;

import java.util.List;

import com.sum.spirit.core.api.ClassResolver;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.element.entity.Element;

public class ClassResolverEntity extends AnnotationEntity {

	public ClassResolver classResolver;

	public ClassResolverEntity(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

}

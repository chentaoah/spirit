package com.sum.spirit.core.clazz.frame;

import java.util.List;

import com.sum.spirit.core.api.ResolverContext;
import com.sum.spirit.core.clazz.entity.IAnnotation;
import com.sum.spirit.core.element.entity.Element;

public abstract class ContextEntity extends AnnotationEntity {

	public ResolverContext context;

	public ContextEntity(List<IAnnotation> annotations, Element element) {
		super(annotations, element);
	}

}

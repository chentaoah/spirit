package com.sum.jass.test.example;

import com.sum.jass.api.convert.AnnotationConverter;
import com.sum.jass.pojo.clazz.IAnnotation;
import com.sum.jass.pojo.clazz.IClass;

public class MyConverter implements AnnotationConverter {

	@Override
	public boolean isMatch(IClass clazz, IAnnotation annotation) {
		return true;
	}

	@Override
	public IAnnotation convert(IClass clazz, IAnnotation annotation) {
		return annotation;
	}

}

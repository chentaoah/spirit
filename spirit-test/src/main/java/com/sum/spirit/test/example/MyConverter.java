package com.sum.spirit.test.example;

import com.sum.spirit.java.api.AnnotationConverter;
import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;

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

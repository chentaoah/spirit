package com.sum.shy.test.example;

import com.sum.shy.api.convert.AnnotationConverter;
import com.sum.shy.pojo.clazz.IAnnotation;
import com.sum.shy.pojo.clazz.IClass;

public class MyConverter implements AnnotationConverter {

	@Override
	public boolean isMatch(IClass clazz, IAnnotation annotation) {
		return true;
	}

	@Override
	public IAnnotation convert(IClass clazz, IAnnotation annotation) {
		System.out.println(annotation);
		return annotation;
	}

}

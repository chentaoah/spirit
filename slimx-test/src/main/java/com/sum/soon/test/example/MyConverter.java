package com.sum.soon.test.example;

import com.sum.soon.api.convert.AnnotationConverter;
import com.sum.soon.pojo.clazz.IAnnotation;
import com.sum.soon.pojo.clazz.IClass;

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

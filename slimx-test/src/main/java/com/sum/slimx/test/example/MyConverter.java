package com.sum.slimx.test.example;

import com.sum.slimx.api.convert.AnnotationConverter;
import com.sum.slimx.pojo.clazz.IAnnotation;
import com.sum.slimx.pojo.clazz.IClass;

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

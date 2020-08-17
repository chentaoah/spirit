package com.sum.jass.api.convert;

import com.sum.jass.pojo.clazz.IAnnotation;
import com.sum.jass.pojo.clazz.IClass;

public interface AnnotationConverter {

	boolean isMatch(IClass clazz, IAnnotation annotation);

	IAnnotation convert(IClass clazz, IAnnotation annotation);

}

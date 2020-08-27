package com.sum.spirit.java.api;

import com.sum.spirit.pojo.clazz.IAnnotation;
import com.sum.spirit.pojo.clazz.IClass;

public interface AnnotationConverter {

	boolean isMatch(IClass clazz, IAnnotation annotation);

	IAnnotation convert(IClass clazz, IAnnotation annotation);

}

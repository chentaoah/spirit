package com.sum.shy.api.convert;

import com.sum.shy.pojo.clazz.IAnnotation;
import com.sum.shy.pojo.clazz.IClass;

public interface AnnotationConverter {

	boolean isMatch(IClass clazz, IAnnotation annotation);

	IAnnotation convert(IClass clazz, IAnnotation annotation);

}

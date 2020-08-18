package com.sum.soon.api.convert;

import com.sum.soon.pojo.clazz.IAnnotation;
import com.sum.soon.pojo.clazz.IClass;

public interface AnnotationConverter {

	boolean isMatch(IClass clazz, IAnnotation annotation);

	IAnnotation convert(IClass clazz, IAnnotation annotation);

}

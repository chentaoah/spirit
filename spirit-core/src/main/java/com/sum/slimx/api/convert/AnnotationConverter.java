package com.sum.slimx.api.convert;

import com.sum.slimx.pojo.clazz.IAnnotation;
import com.sum.slimx.pojo.clazz.IClass;

public interface AnnotationConverter {

	boolean isMatch(IClass clazz, IAnnotation annotation);

	IAnnotation convert(IClass clazz, IAnnotation annotation);

}

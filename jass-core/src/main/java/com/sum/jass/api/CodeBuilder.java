package com.sum.jass.api;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;

@Service("code_builder")
public interface CodeBuilder {

	String build(IClass clazz);

}

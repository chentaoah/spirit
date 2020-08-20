package com.sum.spirit.api;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;

@Service("code_builder")
public interface CodeBuilder {

	String build(IClass clazz);

}

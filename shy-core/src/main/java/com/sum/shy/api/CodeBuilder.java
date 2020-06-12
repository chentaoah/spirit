package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.pojo.clazz.IClass;

@Service("code_builder")
public interface CodeBuilder {

	String build(IClass clazz);

}

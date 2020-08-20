package com.sum.slimx.api;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;

@Service("code_builder")
public interface CodeBuilder {

	String build(IClass clazz);

}

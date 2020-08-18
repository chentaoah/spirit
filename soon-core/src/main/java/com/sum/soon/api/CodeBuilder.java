package com.sum.soon.api;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;

@Service("code_builder")
public interface CodeBuilder {

	String build(IClass clazz);

}

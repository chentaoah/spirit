package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Statement;

public interface FastDeducer {

	IType derive(IClass clazz, Statement statement);

}

package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Statement;

public interface InvokeVisiter {

	void visit(IClass clazz, Statement statement);

}

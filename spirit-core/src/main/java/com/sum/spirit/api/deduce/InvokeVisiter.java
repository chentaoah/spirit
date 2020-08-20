package com.sum.spirit.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.element.Statement;

@Service("invoke_visiter")
public interface InvokeVisiter {

	void visit(IClass clazz, Statement stmt);

}

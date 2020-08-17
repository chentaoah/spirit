package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.element.Statement;

@Service("invoke_visiter")
public interface InvokeVisiter {

	void visit(IClass clazz, Statement stmt);

}

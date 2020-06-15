package com.sum.shy.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.element.Statement;

@Service("invoke_visiter")
public interface InvokeVisiter {

	void visit(IClass clazz, Statement stmt);

}

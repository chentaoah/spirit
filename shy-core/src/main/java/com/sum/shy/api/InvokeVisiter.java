package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.element.Statement;

@Service("invoke_visiter")
public interface InvokeVisiter {

	void visit(IClass clazz, Statement stmt);

}

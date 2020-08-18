package com.sum.soon.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.element.Statement;

@Service("invoke_visiter")
public interface InvokeVisiter {

	void visit(IClass clazz, Statement stmt);

}

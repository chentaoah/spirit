package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.clazz.IType;
import com.sum.jass.pojo.common.MethodContext;
import com.sum.jass.pojo.element.Statement;

@Service("variable_tracker")
public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement stmt);

	IType findType(IClass clazz, MethodContext context, String name);

}

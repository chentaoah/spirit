package com.sum.spirit.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.MethodContext;
import com.sum.spirit.pojo.element.Statement;

@Service("variable_tracker")
public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement statement);

	IType findType(IClass clazz, MethodContext context, String name);

}

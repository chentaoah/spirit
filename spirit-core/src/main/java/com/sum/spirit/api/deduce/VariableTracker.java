package com.sum.spirit.api.deduce;

import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.MethodContext;
import com.sum.spirit.pojo.element.Statement;

public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement statement);

	IType findType(IClass clazz, MethodContext context, String name);

}

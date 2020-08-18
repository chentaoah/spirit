package com.sum.slimx.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.slimx.pojo.clazz.IClass;
import com.sum.slimx.pojo.clazz.IType;
import com.sum.slimx.pojo.common.MethodContext;
import com.sum.slimx.pojo.element.Statement;

@Service("variable_tracker")
public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement stmt);

	IType findType(IClass clazz, MethodContext context, String name);

}

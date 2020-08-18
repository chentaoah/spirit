package com.sum.soon.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.common.MethodContext;
import com.sum.soon.pojo.element.Statement;

@Service("variable_tracker")
public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement stmt);

	IType findType(IClass clazz, MethodContext context, String name);

}

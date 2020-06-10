package com.sum.shy.api.deducer;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.common.MethodContext;
import com.sum.shy.element.Statement;

@Service("variable_tracker")
public interface VariableTracker {

	void track(IClass clazz, MethodContext context, Statement stmt);

	IType findType(IClass clazz, MethodContext context, String name);

}

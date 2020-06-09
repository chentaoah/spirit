package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.api.service.MemberVisiterImpl.MethodContext;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Stmt;

@Service("variable_tracker")
public interface VariableTracker {

	void trackStmt(IClass clazz, MethodContext context, Stmt stmt);

	IType findType(IClass clazz, MethodContext context, String name);

}

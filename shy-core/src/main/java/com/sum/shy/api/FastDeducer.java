package com.sum.shy.api;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType deriveStmt(IClass clazz, Statement stmt);

}

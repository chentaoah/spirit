package com.sum.shy.api.deducer;

import com.sum.pisces.api.Service;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType derive(IClass clazz, Statement stmt);

}

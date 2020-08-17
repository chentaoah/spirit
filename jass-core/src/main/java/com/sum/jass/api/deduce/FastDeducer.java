package com.sum.shy.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType derive(IClass clazz, Statement stmt);

}

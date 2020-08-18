package com.sum.jass.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.jass.pojo.clazz.IClass;
import com.sum.jass.pojo.clazz.IType;
import com.sum.jass.pojo.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType derive(IClass clazz, Statement stmt);

}

package com.sum.spirit.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType derive(IClass clazz, Statement stmt);

}

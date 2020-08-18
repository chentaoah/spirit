package com.sum.soon.api.deduce;

import com.sum.pisces.api.annotation.Service;
import com.sum.soon.pojo.clazz.IClass;
import com.sum.soon.pojo.clazz.IType;
import com.sum.soon.pojo.element.Statement;

@Service("fast_deducer")
public interface FastDeducer {

	IType derive(IClass clazz, Statement stmt);

}

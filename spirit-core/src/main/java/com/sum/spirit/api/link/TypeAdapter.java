package com.sum.spirit.api.link;

import com.sum.pisces.api.annotation.Service;
import com.sum.spirit.pojo.clazz.IType;

@Service("type_adapter")
public interface TypeAdapter {

	IType adapte(IType type);

}

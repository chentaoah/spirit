package com.sum.spirit.api.link;

import com.sum.spirit.pojo.clazz.IType;

public interface TypeAdapter {

	IType adapte(String name);

	String provide(String simpleName);

}

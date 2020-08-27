package com.sum.spirit.api.link;

import com.sum.spirit.pojo.clazz.IType;

public interface NativeLoader {

	IType loadType(String name);

	String findCommonType(String simpleName);

}

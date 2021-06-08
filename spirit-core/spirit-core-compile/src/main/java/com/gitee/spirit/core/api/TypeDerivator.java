package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;

public interface TypeDerivator {

	Integer getAbstractDegree(IType abstractType, IType targetType);

	boolean isMoreAbstract(IType abstractType, IType targetType);

	IType populate(IType instanceType, IType targetType);

}

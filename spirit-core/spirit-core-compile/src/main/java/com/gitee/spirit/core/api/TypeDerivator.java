package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;

public interface TypeDerivator {

	IType populate(IType instanceType, IType targetType);

	Integer getAbstractScore(IType abstractType, IType targetType);

	boolean isMoreAbstract(IType abstractType, IType targetType);

}

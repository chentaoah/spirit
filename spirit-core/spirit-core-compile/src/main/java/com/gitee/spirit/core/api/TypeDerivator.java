package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;

public interface TypeDerivator {

    IType populate(IType instanceType, IType targetType);

    boolean similar(IType targetType1, IType targetType2);

    IType findReferenceType(IType targetType, IType referenceType);

    Integer getAbstractDegree(IType abstractType, IType targetType);

    boolean isMoreAbstract(IType abstractType, IType targetType);

}

package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;

import java.util.List;

public interface TypeDerivator {

    IType populate(IType instanceType, IType targetType);

    boolean isSimilar(IType targetType1, IType targetType2);

    IType findTypeByInherit(IType instanceType, IType targetType);

    Integer getAbstractDegree(IType abstractType, IType targetType);

    Integer getMatchingDegree(List<IType> parameterTypes, List<IType> methodParameterTypes);

    boolean isMoreAbstract(IType abstractType, IType targetType);

}

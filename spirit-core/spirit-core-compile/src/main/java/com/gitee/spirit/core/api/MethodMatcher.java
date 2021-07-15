package com.gitee.spirit.core.api;

import com.gitee.spirit.core.clazz.entity.IType;

import java.util.List;

public interface MethodMatcher<M, R> {

    boolean checkParameterCount(M method, List<IType> parameterTypes);

    R getParameterTypes(IType type, M method, List<IType> parameterTypes);

    R findMethod(IType type, List<M> methods, List<IType> parameterTypes);

}

package com.gitee.spirit.core.compile.linker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.core.compile.entity.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.TypeDerivator;
import com.gitee.spirit.core.clazz.entity.IMethod;
import com.gitee.spirit.core.clazz.entity.IParameter;
import com.gitee.spirit.core.clazz.entity.IType;

@Component
public class AppMethodMatcher {

    @Autowired
    public TypeDerivator derivator;

    public boolean checkParameterCount(IMethod method, List<IType> parameterTypes) {
        return method.parameters.size() == parameterTypes.size();
    }

    public MatchResult getParameterTypes(IType type, IMethod method, List<IType> parameterTypes) {
        if (!checkParameterCount(method, parameterTypes)) {
            return null;
        }
        List<IType> methodParameterTypes = method.getParameterTypes();
        for (int index = 0; index < parameterTypes.size(); index++) {
            IType methodParameterType = derivator.populate(type, methodParameterTypes.get(index));
            methodParameterTypes.set(index, methodParameterType);
        }
        return new MatchResult(method, methodParameterTypes);
    }

    public Integer getMethodScore(List<IType> parameterTypes, List<IType> methodParameterTypes) {
        Integer finalScore = 0;
        for (int index = 0; index < parameterTypes.size(); index++) {
            Integer scope = derivator.getAbstractDegree(methodParameterTypes.get(index), parameterTypes.get(index));
            finalScore = scope != null ? finalScore + scope : null;
            if (finalScore == null) {
                return null;
            }
        }
        return finalScore;
    }

    public MatchResult findMethod(IType type, List<IMethod> methods, List<IType> parameterTypes) {
        Map<IMethod, MatchResult> matchResultMap = new HashMap<>();
        IMethod method = ListUtils.findOneByScore(methods, eachMethod -> {
            MatchResult matchResult = getParameterTypes(type, eachMethod, parameterTypes);
            matchResultMap.put(eachMethod, matchResult);
            return getMethodScore(parameterTypes, matchResult.parameterTypes);
        });
        return matchResultMap.get(method);
    }

}

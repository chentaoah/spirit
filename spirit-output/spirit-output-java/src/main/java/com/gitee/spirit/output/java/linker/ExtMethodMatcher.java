package com.gitee.spirit.output.java.linker;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

import com.gitee.spirit.common.utils.ListUtils;
import com.gitee.spirit.output.java.entity.MatchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.output.java.ExtTypeDerivator;
import com.gitee.spirit.output.java.ExtTypeFactory;
import com.gitee.spirit.output.java.utils.ReflectUtils;

@Component
public class ExtMethodMatcher {

    @Autowired
    public ExtTypeFactory factory;
    @Autowired
    public ExtTypeDerivator derivator;

    public boolean checkParameterCount(Method method, List<IType> parameterTypes) {
        if (!ReflectUtils.isIndefinite(method) && parameterTypes.size() == method.getParameterCount()) {// 不是不定项，那么参数个数相等
            return true;
        } else if (ReflectUtils.isIndefinite(method) && parameterTypes.size() >= method.getParameterCount() - 1) {// 不定项，则参数大于等于不定项-1
            return true;
        }
        return false;
    }

    public MatchResult getParameterTypes(IType type, Method method, List<IType> parameterTypes) {
        if (!checkParameterCount(method, parameterTypes)) {
            return null;
        }
        List<IType> nativeParameterTypes = new ArrayList<>();
        Map<String, IType> qualifyingTypes = new HashMap<>();
        for (int index = 0; index < parameterTypes.size(); index++) {
            IType parameterType = parameterTypes.get(index);
            //保证索引不会溢出
            int idx = Math.min(index, method.getParameterCount() - 1);
            //分为两种情况，一种是最后一个参数之前的，一种是最后一个参数
            Parameter parameter = method.getParameters()[idx];
            //获取本地参数类型
            IType nativeParameterType = factory.create(parameter.getParameterizedType());
            //如果最后一个参数，而且是不定项参数，则取数组里的类型
            if (idx == method.getParameterCount() - 1 && ReflectUtils.isIndefinite(parameter)) {
                nativeParameterType = nativeParameterType.toTarget();
            }
            //从继承关系中，找出适当的类型
            parameterType = derivator.findReferenceType(parameterType, nativeParameterType);
            //没有找到对应的，则直接返回
            if (parameterType == null) {
                return null;
            }
            //如果因为限定冲突，则直接返回null
            try {
                nativeParameterType = derivator.populateQualifying(type, parameterType, nativeParameterType, qualifyingTypes);
            } catch (IllegalArgumentException e) {
                return null;
            }
            //添加到待返回的集合中
            nativeParameterTypes.add(nativeParameterType);
        }
        return new MatchResult(method, nativeParameterTypes, qualifyingTypes);
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

    public MatchResult findMethod(IType type, List<Method> methods, List<IType> parameterTypes) {
        Map<Method, MatchResult> matchResultMap = new HashMap<>();
        Method method = ListUtils.findOneByScore(methods, eachMethod -> {
            MatchResult matchResult = getParameterTypes(type, eachMethod, parameterTypes);
            if (matchResult != null) {
                matchResultMap.put(eachMethod, matchResult);
                return getMethodScore(parameterTypes, matchResult.parameterTypes);
            }
            return null;
        });
        return matchResultMap.get(method);
    }

}

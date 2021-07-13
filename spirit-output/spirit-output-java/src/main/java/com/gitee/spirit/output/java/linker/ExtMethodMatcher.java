package com.gitee.spirit.output.java.linker;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            //如果结构不同，则直接返回不匹配
            if (!parameterType.similar(nativeParameterType)) {
                return null;
            }
            //填充类型里的泛型参数
            if (!parameterType.isNull()) {
                nativeParameterType = derivator.populateQualifying(type, parameterType, nativeParameterType, qualifyingTypes);
            }
            //添加到待返回的集合中
            nativeParameterTypes.add(nativeParameterType);
        }
        return new MatchResult(nativeParameterTypes, qualifyingTypes);
    }

    public Integer getMethodScore(IType type, Method method, List<IType> parameterTypes) {
        List<IType> nativeParameterTypes = getParameterTypes(type, method, parameterTypes).parameterTypes;
        if (nativeParameterTypes == null) {
            return null;
        }
        Integer finalScore = 0;
        for (int index = 0; index < parameterTypes.size(); index++) {
            Integer scope = derivator.getAbstractDegree(nativeParameterTypes.get(index), parameterTypes.get(index));
            finalScore = scope != null ? finalScore + scope : null;
            if (finalScore == null) {
                return null;
            }
        }
        return finalScore;
    }

}

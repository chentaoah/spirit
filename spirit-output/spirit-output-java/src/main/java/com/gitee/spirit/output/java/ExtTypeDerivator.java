package com.gitee.spirit.output.java;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.TypeBuilder;
import com.gitee.spirit.core.clazz.utils.TypeVisitor;
import com.gitee.spirit.core.compile.derivator.AppTypeDerivator;

@Component
public class ExtTypeDerivator extends AppTypeDerivator {

    @Autowired
    public ClassLinker linker;

    @Override
    public IType populate(IType instanceType, IType targetType) {
        return TypeVisitor.forEachType(targetType, eachType -> {
            if (eachType.isTypeVariable()) {
                int index = linker.getTypeVariableIndex(instanceType, eachType.getGenericName());
                if (index >= 0) {
                    return TypeBuilder.copy(instanceType.getGenericTypes().get(index));
                }
            }
            return eachType;
        });
    }

    public IType populateParameter(IType type, IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
        targetType = populate(type, targetType);
        targetType = populateQualifying(parameterType, targetType, qualifyingTypes);
        return targetType;
    }

    public IType populateQualifying(IType parameterType, IType targetType, Map<String, IType> qualifyingTypes) {
        return TypeVisitor.forEachType(parameterType, targetType, (referType, eachType) -> {
            if (eachType.isTypeVariable()) {
                String genericName = eachType.getGenericName();
                if (qualifyingTypes.containsKey(genericName)) {// 如果已经存在了，则必须统一
                    IType existType = qualifyingTypes.get(genericName);
                    if (!existType.equals(referType)) {
                        throw new IllegalArgumentException("Parameter qualification types are not uniform!");
                    }
                    return TypeBuilder.copy(referType);

                } else {
                    referType = TypeBuilder.copy(referType);
                    qualifyingTypes.put(genericName, referType);
                    return referType;
                }
            }
            return eachType;
        });
    }

    public IType populateReturnType(IType type, Map<String, IType> qualifyingTypes, IType targetType) {
        targetType = populate(type, targetType);
        targetType = populateByQualifying(qualifyingTypes, targetType);
        return targetType;
    }

    public IType populateByQualifying(Map<String, IType> qualifyingTypes, IType targetType) {
        return TypeVisitor.forEachType(targetType, eachType -> {
            if (eachType.isTypeVariable()) {
                return qualifyingTypes.get(targetType.getGenericName());
            }
            return eachType;
        });
    }
}

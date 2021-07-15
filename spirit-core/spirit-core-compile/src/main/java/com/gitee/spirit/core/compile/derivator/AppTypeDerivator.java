package com.gitee.spirit.core.compile.derivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.TypeDerivator;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.TypeBuilder;
import com.gitee.spirit.core.clazz.utils.TypeVisitor;

import cn.hutool.core.lang.Assert;

import java.util.List;

@Primary
@Component
public class AppTypeDerivator implements TypeDerivator {

    @Autowired
    public ClassLinker linker;

    @Override
    public IType populate(IType instanceType, IType targetType) {
        // 根据全局类型，进行填充
        return TypeVisitor.forEachType(targetType, eachType -> {
            if (eachType.isTypeVariable()) {
                int index = linker.getTypeVariableIndex(instanceType, eachType.getGenericName());
                Assert.isTrue(index >= 0, "Index of type variable less than 0!");
                Assert.isTrue(instanceType.isGenericType(), "Type must be a generic type!");
                return TypeBuilder.copy(instanceType.getGenericTypes().get(index));
            }
            return eachType;
        });
    }

    @Override
    public boolean isSimilar(IType targetType1, IType targetType2) {
        String finalName1 = TypeVisitor.forEachTypeName(targetType1, eachType -> "");
        String finalName2 = TypeVisitor.forEachTypeName(targetType2, eachType -> "");
        return finalName1.equals(finalName2);
    }

    @Override
    public IType upcastTo(IType instanceType, IType targetType) {
        if (instanceType == null || targetType == null) {
            return null;
        }
        if (instanceType.isNull()) {
            return targetType;
        }
        if (instanceType.getClassName().equals(targetType.getClassName())) {
            Assert.isTrue(isSimilar(instanceType, targetType), "The two types must be structurally similar!");
            return instanceType;
        }
        IType superType = upcastTo(linker.getSuperType(instanceType.toBox()), targetType);
        if (superType != null) {
            return superType;
        }
        for (IType interfaceType : linker.getInterfaceTypes(instanceType)) {
            interfaceType = upcastTo(interfaceType, targetType);
            if (interfaceType != null) {
                return interfaceType;
            }
        }
        return null;
    }

    @Override
    public Integer getAbstractDegree(IType abstractType, IType targetType) {
        if (abstractType == null || targetType == null) {
            return null;
        }
        // null类型不能比任何类型抽象
        if (abstractType.isNull()) {
            return null;
        }
        // 任何类型都能比null抽象
        if (targetType.isNull()) {
            return 0;
        }
        // 这个方法还要判断泛型
        if (targetType.equals(abstractType)) {
            return 0;
        }
        // 这个方法中，还要考虑到自动拆组包
        Integer superTypeDegree = getAbstractDegree(abstractType, linker.getSuperType(targetType.toBox()));
        if (superTypeDegree != null) {
            return superTypeDegree - 1;
        }
        // 接口
        for (IType interfaceType : linker.getInterfaceTypes(targetType)) {
            Integer interfaceTypeDegree = getAbstractDegree(abstractType, interfaceType);
            if (interfaceTypeDegree != null) {
                return interfaceTypeDegree - 1;
            }
        }
        return null;
    }

    @Override
    public Integer getMatchingDegree(List<IType> parameterTypes, List<IType> methodParameterTypes) {
        Integer finalDegree = 0;
        for (int index = 0; index < parameterTypes.size(); index++) {
            Integer degree = getAbstractDegree(methodParameterTypes.get(index), parameterTypes.get(index));
            finalDegree = degree != null ? finalDegree + degree : null;
            if (finalDegree == null) {
                return null;
            }
        }
        return finalDegree;
    }

    @Override
    public boolean isMoreAbstract(IType abstractType, IType targetType) {
        return getAbstractDegree(abstractType, targetType) != null;
    }


}

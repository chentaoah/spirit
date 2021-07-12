package com.gitee.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Dictionary;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.api.TypeFactory;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.CommonTypes;

@Component
public class PrimitiveClassLinker implements ClassLinker {

    @Autowired
    public TypeFactory factory;

    @Override
    public <T> T toClass(IType type) {
        throw new RuntimeException("This method is not supported!");
    }

    @Override
    public int getTypeVariableIndex(IType type, String genericName) {
        throw new RuntimeException("This method is not supported!");
    }

    @Override
    public IType getSuperType(IType type) {
        return null;
    }

    @Override
    public List<IType> getInterfaceTypes(IType type) {
        return new ArrayList<>();
    }

    @Override
    public IType visitField(IType type, String fieldName) {
        if (Dictionary.CLASS.equals(fieldName)) {
            return factory.create(CommonTypes.CLASS.getClassName(), type.toBox());
        } else {
            throw new RuntimeException("The primitive type has no other fields!");
        }
    }

    @Override
    public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) {
        throw new RuntimeException("The primitive type has no method!");
    }

    @Override
    public List<IType> getParameterTypes(IType type, String methodName, List<IType> parameterTypes) {
        throw new RuntimeException("The array type has no method!");
    }

}

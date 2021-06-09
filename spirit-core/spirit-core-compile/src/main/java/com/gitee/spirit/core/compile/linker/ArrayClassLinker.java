package com.gitee.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.gitee.spirit.common.constants.Dictionary;
import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.CommonTypes;

@Component
public class ArrayClassLinker implements ClassLinker {

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
		return CommonTypes.OBJECT;
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		return new ArrayList<>();
	}

	@Override
	public IType visitField(IType type, String fieldName) throws NoSuchFieldException {
		if (Dictionary.LENGTH.equals(fieldName)) {
			return CommonTypes.INT;
		} else {
			throw new RuntimeException("The array type has no other fields");
		}
	}

	@Override
	public IType visitMethod(IType type, String methodName, List<IType> parameterTypes) throws NoSuchMethodException {
		throw new RuntimeException("The array type has no method!");
	}

}

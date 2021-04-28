package com.sum.spirit.output.java.linker;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.output.java.ExtClassLoader;
import com.sum.spirit.output.java.deduce.NativeTypeDerivator;
import com.sum.spirit.output.java.deduce.NativeTypeFactory;

public abstract class AbstractNativeClassLinker implements ClassLinker {

	@Autowired
	public ExtClassLoader classLoader;
	@Autowired
	public NativeTypeFactory factory;
	@Autowired
	public NativeTypeDerivator derivator;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T toClass(IType type) {
		return (T) classLoader.findClass(type.getClassName());// 可能是数组
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		Class<?> clazz = toClass(type);
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		for (int index = 0; index < typeVariables.length; index++) {
			TypeVariable<?> typeVariable = typeVariables[index];
			if (typeVariable.toString().equals(genericName)) {
				return index;
			}
		}
		return -1;
	}

	@Override
	public IType getSuperType(IType type) {
		Class<?> clazz = toClass(type);
		Type nativeSuperType = clazz.getGenericSuperclass();
		IType superType = nativeSuperType != null ? factory.create(nativeSuperType) : null;
		return derivator.populate(type, superType);
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		Class<?> clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (Type interfaceType : clazz.getGenericInterfaces()) {
			interfaceTypes.add(derivator.populate(type, factory.create(interfaceType)));
		}
		return interfaceTypes;
	}

}

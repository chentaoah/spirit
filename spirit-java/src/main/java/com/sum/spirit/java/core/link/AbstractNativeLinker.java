package com.sum.spirit.java.core.link;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.java.ExtClassLoader;
import com.sum.spirit.utils.SpringUtils;

public abstract class AbstractNativeLinker implements ClassLinker {

	@Autowired
	public ExtClassLoader classLoader;
	@Autowired
	public NativeFactory factory;

	@Override
	public boolean canLink(IType type) {
		return type.isNative();
	}

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
		return factory.populate(type, superType);
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		Class<?> clazz = toClass(type);
		List<IType> interfaceTypes = new ArrayList<>();
		for (Type interfaceType : clazz.getGenericInterfaces()) {
			interfaceTypes.add(factory.populate(type, factory.create(interfaceType)));
		}
		return interfaceTypes;
	}

	@Override
	public boolean isMoreAbstract(IType abstractType, IType type) {
		ClassLinker linker = SpringUtils.getBean(ClassLinker.class);
		return linker.isMoreAbstract(abstractType, type);
	}

}

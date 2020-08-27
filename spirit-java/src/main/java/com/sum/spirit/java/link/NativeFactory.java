package com.sum.spirit.java.link;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sum.spirit.core.link.TypeFactoryImpl;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.common.TypeTable;

@Component
public class NativeFactory extends TypeFactoryImpl {

	public IType create(Class<?> clazz) {
		IType type = create(clazz.getName());
		TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
		if (typeVariables != null && typeVariables.length > 0) {
			List<IType> genericTypes = new ArrayList<>();
			for (TypeVariable<?> typeVariable : typeVariables)
				genericTypes.add(createTypeVariable(typeVariable.toString()));
			// Note that this is a non modifiable list
			type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		}
		return type;
	}

	public IType create(Type nativeType) {
		if (nativeType instanceof Class) {// String
			return create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// ?
			return TypeTable.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// T or K
			return createTypeVariable(nativeType.toString());

		} else if (nativeType instanceof ParameterizedType) {// List<T>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments())
				genericTypes.add(create(actualType));
			return create(rawType.getName(), genericTypes);
		}
		throw new RuntimeException("Unknown type!");
	}

}

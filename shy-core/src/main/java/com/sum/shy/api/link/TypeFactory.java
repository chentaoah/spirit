package com.sum.shy.api.link;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.api.lexer.SemanticParser;
import com.sum.shy.lib.Assert;
import com.sum.shy.pojo.clazz.IClass;
import com.sum.shy.pojo.clazz.IType;
import com.sum.shy.pojo.common.StaticType;
import com.sum.shy.pojo.element.Token;

@Service("type_factory")
public interface TypeFactory {

	public static SemanticParser parser = ProxyFactory.get(SemanticParser.class);

	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);

	default IType create(Class<?> clazz) {
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

	default IType createTypeVariable(String genericName) {// T or K
		IType type = create(Object.class.getName());
		type.setGenericName(genericName);
		return type;
	}

	default IType create(Class<?> clazz, IType... genericTypes) {
		return create(clazz, Arrays.asList(genericTypes));
	}

	default IType create(Class<?> clazz, List<IType> genericTypes) {
		IType type = create(clazz.getName());
		type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return type;
	}

	default IType create(Type nativeType) {
		if (nativeType instanceof Class) {// String
			return create((Class<?>) nativeType);

		} else if (nativeType instanceof WildcardType) {// ?
			return StaticType.WILDCARD_TYPE;

		} else if (nativeType instanceof TypeVariable) {// T or K
			return createTypeVariable(nativeType.toString());

		} else if (nativeType instanceof ParameterizedType) {// List<T>
			ParameterizedType parameterizedType = (ParameterizedType) nativeType;
			Class<?> rawType = (Class<?>) parameterizedType.getRawType();
			List<IType> genericTypes = new ArrayList<>();
			for (Type actualType : parameterizedType.getActualTypeArguments())
				genericTypes.add(create(actualType));
			return create(rawType, genericTypes);
		}
		throw new RuntimeException("Unknown type!");
	}

	default IType populate(IType type, IType superType) {

		if (superType == null)
			return null;

		superType = superType.copy();

		if (superType.isGenericType()) {
			List<IType> genericTypes = new ArrayList<>();
			for (IType genericType : superType.getGenericTypes())
				genericTypes.add(populate(type, genericType));
			superType.setGenericTypes(Collections.unmodifiableList(genericTypes));

		} else if (superType.isTypeVariable()) {
			// If it is a generic parameter, it is derived by the type passed in
			int index = linker.getTypeVariableIndex(type, superType.getGenericName());
			Assert.isTrue(index >= 0, "Index of type variable less than 0!");
			Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
			List<IType> genericTypes = type.getGenericTypes();
			return genericTypes.get(index).copy();
		}
		return superType;
	}

	default IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text));
	}

	IType create(String className);

	IType create(IClass clazz, Token token);

}

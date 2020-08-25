package com.sum.spirit.api.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sum.pisces.api.annotation.Service;
import com.sum.pisces.core.ProxyFactory;
import com.sum.spirit.api.lexer.SemanticParser;
import com.sum.spirit.lib.Assert;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.clazz.IType;
import com.sum.spirit.pojo.element.Token;

@Service("type_factory")
public interface TypeFactory {

	public static SemanticParser parser = ProxyFactory.get(SemanticParser.class);
	public static ClassLinker linker = ProxyFactory.get(ClassLinker.class);

	default IType create(String className, IType... genericTypes) {
		return create(className, Arrays.asList(genericTypes));
	}

	default IType create(String className, List<IType> genericTypes) {
		IType type = create(className);
		type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return type;
	}

	default IType createTypeVariable(String genericName) {// T or K
		IType type = create(Object.class.getName());
		type.setGenericName(genericName);
		return type;
	}

	default IType populate(IType type, IType targetType) {

		if (targetType == null)
			return null;

		targetType = targetType.copy();

		if (targetType.isGenericType()) {
			List<IType> genericTypes = new ArrayList<>();
			for (IType genericType : targetType.getGenericTypes())
				genericTypes.add(populate(type, genericType));
			targetType.setGenericTypes(Collections.unmodifiableList(genericTypes));

		} else if (targetType.isTypeVariable()) {
			// If it is a generic parameter, it is derived by the type passed in
			int index = linker.getTypeVariableIndex(type, targetType.getGenericName());
			Assert.isTrue(index >= 0, "Index of type variable less than 0!");
			Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
			List<IType> genericTypes = type.getGenericTypes();
			return genericTypes.get(index).copy();
		}
		return targetType;
	}

	default IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text));
	}

	IType create(String className);

	IType create(IClass clazz, Token token);

}

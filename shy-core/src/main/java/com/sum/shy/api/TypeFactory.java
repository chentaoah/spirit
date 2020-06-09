package com.sum.shy.api;

import java.util.List;

import com.sum.pisces.api.Service;
import com.sum.pisces.core.ProxyFactory;
import com.sum.shy.clazz.IClass;
import com.sum.shy.clazz.IType;
import com.sum.shy.element.Token;
import com.sum.shy.lib.Assert;

@Service("type_factory")
public interface TypeFactory {

	public static SemanticParser parser = ProxyFactory.get(SemanticParser.class);

	default IType create(Class<?> clazz) {
		return create(clazz.getName());
	}

	default IType create(Class<?> clazz, List<IType> genericTypes) {
		IType type = create(clazz);
		type.setGenericTypes(genericTypes);
		return type;
	}

	default IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text));
	}

	IType create(String className);

	IType create(IClass clazz, Token token);

}

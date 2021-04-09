package com.sum.spirit.core.compile.deduce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.entity.StaticTypes;

import cn.hutool.core.lang.Assert;

public abstract class AbstractTypeFactory implements TypeFactory {

	@Autowired
	public SemanticParser parser;

	public IType create(String className, IType... genericTypes) {
		return create(className, Arrays.asList(genericTypes));
	}

	public IType create(String className, List<IType> genericTypes) {
		IType type = create(className);
		type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return type;
	}

	public IType createTypeVariable(String genericName) {// T or K
		IType type = create(StaticTypes.OBJECT.getClassName());
		type.setGenericName(genericName);
		return type;
	}

	public IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text));
	}

}

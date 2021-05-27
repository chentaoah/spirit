package com.sum.spirit.core.clazz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.api.SemanticParser;
import com.sum.spirit.core.api.TypeFactory;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeTable;

import cn.hutool.core.lang.Assert;

public abstract class AbstractTypeFactory implements TypeFactory {

	@Autowired
	public SemanticParser parser;

	@Override
	public IType create(String className, IType... genericTypes) {
		return create(className, Arrays.asList(genericTypes));
	}

	@Override
	public IType create(String className, List<IType> genericTypes) {
		IType type = create(className);
		type.setGenericTypes(Collections.unmodifiableList(genericTypes));
		return type;
	}

	@Override
	public IType createTypeVariable(String genericName) {// T or K
		IType type = create(TypeTable.OBJECT.getClassName());
		type.setGenericName(genericName);
		return type;
	}

	@Override
	public IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text));
	}

}

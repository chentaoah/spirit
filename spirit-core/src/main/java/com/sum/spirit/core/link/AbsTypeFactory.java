package com.sum.spirit.core.link;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.build.SemanticParser;
import com.sum.spirit.pojo.clazz.impl.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.impl.Token;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.TypeBuilder;
import com.sum.spirit.utils.TypeVisiter;

import cn.hutool.core.lang.Assert;

public abstract class AbsTypeFactory {

	@Autowired
	public ClassLinker linker;
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
		IType type = create(TypeEnum.Object.value.getClassName());
		type.setGenericName(genericName);
		return type;
	}

	public IType populate(IType type, IType targetType) {
		// 使用匿名表达式
		return new TypeVisiter().visit(targetType, (rawType, index, currentType) -> {
			if (currentType.isTypeVariable()) {
				int idx = linker.getTypeVariableIndex(type, currentType.getGenericName());
				if (checkIndex(type, idx)) {
					return TypeBuilder.copy(type.getGenericTypes().get(idx));
				}
			}
			return null;
		});
	}

	public boolean checkIndex(IType type, int index) {
		Assert.isTrue(index >= 0, "Index of type variable less than 0!");
		Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
		return true;
	}

	public IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text, false));
	}

	public abstract IType create(String className);

	public abstract IType create(IClass clazz, Token token);

}

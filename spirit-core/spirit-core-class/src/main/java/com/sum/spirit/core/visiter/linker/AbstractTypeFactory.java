package com.sum.spirit.core.visiter.linker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.element.action.SemanticParser;
import com.sum.spirit.core.element.entity.Token;
import com.sum.spirit.core.utils.TypeBuilder;
import com.sum.spirit.core.visiter.entity.IType;
import com.sum.spirit.core.visiter.enums.TypeEnum;
import com.sum.spirit.core.visiter.utils.TypeVisiter;

import cn.hutool.core.lang.Assert;

public abstract class AbstractTypeFactory {

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

	public IType populate(IType type, IType targetType) {// 根据全局类型，进行填充
		return new TypeVisiter().visit(targetType, event -> {
			IType currentType = event.item;
			if (currentType.isTypeVariable()) {
				int index = linker.getTypeVariableIndex(type, currentType.getGenericName());
				if (checkIndex(type, index)) {
					return TypeBuilder.copy(type.getGenericTypes().get(index));
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

package com.sum.spirit.core.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.build.SemanticParser;
import com.sum.spirit.pojo.clazz.IClass;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.element.Token;
import com.sum.spirit.pojo.enums.TypeEnum;
import com.sum.spirit.utils.Assert;
import com.sum.spirit.utils.SpringUtils;
import com.sum.spirit.utils.TypeBuilder;

public abstract class AbsTypeFactory {

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

		if (targetType == null) {
			return null;
		}
		// 拷贝一份
		targetType = TypeBuilder.copy(targetType);

		if (targetType.isGenericType()) {
			List<IType> genericTypes = new ArrayList<>();
			for (IType genericType : targetType.getGenericTypes()) {
				genericTypes.add(populate(type, genericType));
			}
			targetType.setGenericTypes(Collections.unmodifiableList(genericTypes));

		} else if (targetType.isTypeVariable()) {
			ClassLinker linker = SpringUtils.getBean("adaptiveLinker", ClassLinker.class);
			int index = linker.getTypeVariableIndex(type, targetType.getGenericName());
			Assert.isTrue(index >= 0, "Index of type variable less than 0!");
			Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
			List<IType> genericTypes = type.getGenericTypes();
			// 拷贝一份
			return TypeBuilder.copy(genericTypes.get(index));
		}

		return targetType;
	}

	public IType create(IClass clazz, String text) {
		Assert.isTrue(!text.contains("."), "Text cannot contains \".\". Please use the another create method!");
		return create(clazz, parser.getToken(text, false));
	}

	public abstract IType create(String className);

	public abstract IType create(IClass clazz, Token token);

}

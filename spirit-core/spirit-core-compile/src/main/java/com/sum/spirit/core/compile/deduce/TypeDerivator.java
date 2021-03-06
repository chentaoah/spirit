package com.sum.spirit.core.compile.deduce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.TypeBuilder;
import com.sum.spirit.core.clazz.utils.TypeVisiter;
import com.sum.spirit.core.compile.entity.StaticTypes;
import com.sum.spirit.core.element.entity.Token;

import cn.hutool.core.lang.Assert;

@Component
public class TypeDerivator {

	@Autowired
	public TypeFactory factory;
	@Autowired
	public ClassLinker linker;

	public IType toBox(IType type) {
		IType boxType = StaticTypes.getBoxType(type.getClassName());
		return boxType != null ? boxType : type;
	}

	public IType toTarget(IType type) {
		return factory.create(type.getTargetName());
	}

	public IType withSuperModifiers(IType type) {
		type.setModifiers(ModifierEnum.SUPER.value);
		return type;
	}

	public IType withThisModifiers(IType type) {
		type.setModifiers(ModifierEnum.THIS.value);
		return type;
	}

	public IType populateByInstanceType(IType type, IType targetType) {// 根据全局类型，进行填充
		return TypeVisiter.visit(targetType, eachType -> {
			if (eachType.isTypeVariable()) {
				int index = linker.getTypeVariableIndex(type, eachType.getGenericName());
				Assert.isTrue(index >= 0, "Index of type variable less than 0!");
				Assert.isTrue(type.isGenericType(), "Type must be a generic type!");
				return TypeBuilder.copy(type.getGenericTypes().get(index));
			}
			return eachType;
		});
	}

	public IType getSuperType(IClass clazz) {// 注意:这里返回的是Super<T,K>
		Token token = clazz.element.getKeywordParam(KeywordEnum.EXTENDS.value);// 这里返回的,可以是泛型格式，而不是className
		if (token != null) {
			return factory.create(clazz, token);
		}
		return StaticTypes.OBJECT;// 如果不存在继承，则默认是继承Object
	}

	public List<IType> getInterfaceTypes(IClass clazz) {
		List<IType> interfaces = new ArrayList<>();
		for (Token token : clazz.element.getKeywordParams(KeywordEnum.IMPLS.value)) {
			interfaces.add(factory.create(clazz, token));
		}
		return interfaces;
	}

}

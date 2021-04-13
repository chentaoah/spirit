package com.sum.spirit.core.compile.deduce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.clazz.utils.StaticTypes;
import com.sum.spirit.core.clazz.utils.TypeBuilder;
import com.sum.spirit.core.clazz.utils.TypeVisiter;
import com.sum.spirit.core.compile.AppTypeFactory;

import cn.hutool.core.lang.Assert;

@Component
@Primary
public class TypeDerivator {

	@Autowired
	public AppTypeFactory factory;
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

	public IType populate(IType instanceType, IType targetType) {// 根据全局类型，进行填充
		return TypeVisiter.visit(targetType, eachType -> {
			if (eachType.isTypeVariable()) {
				int index = linker.getTypeVariableIndex(instanceType, eachType.getGenericName());
				Assert.isTrue(index >= 0, "Index of type variable less than 0!");
				Assert.isTrue(instanceType.isGenericType(), "Type must be a generic type!");
				return TypeBuilder.copy(instanceType.getGenericTypes().get(index));
			}
			return eachType;
		});
	}

	public boolean isMoreAbstract(IType abstractType, IType type) {
		if (type == null) {
			return false;
		}
		if (abstractType.isNull()) {// null类型不能比任何类型抽象
			return false;
		}
		if (type.isNull()) {// 任何类型都能比null抽象
			return true;
		}
		if (type.equals(abstractType)) {// 这个方法还要判断泛型
			return true;
		}
		if (isMoreAbstract(abstractType, linker.getSuperType(toBox(type)))) {// 这个方法中，还要考虑到自动拆组包
			return true;
		}
		for (IType inter : linker.getInterfaceTypes(type)) {// 接口
			if (isMoreAbstract(abstractType, inter)) {
				return true;
			}
		}
		return false;
	}

}

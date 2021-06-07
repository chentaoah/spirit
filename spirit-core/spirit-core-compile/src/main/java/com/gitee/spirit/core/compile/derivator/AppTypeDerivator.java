package com.gitee.spirit.core.compile.derivator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.gitee.spirit.core.api.ClassLinker;
import com.gitee.spirit.core.clazz.entity.IType;
import com.gitee.spirit.core.clazz.utils.TypeBuilder;
import com.gitee.spirit.core.clazz.utils.TypeVisiter;

import cn.hutool.core.lang.Assert;

@Primary
@Component
public class AppTypeDerivator {

	@Autowired
	public ClassLinker linker;

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

	public Integer getAbstractScore(IType abstractType, IType type) {
		if (abstractType == null || type == null) {
			return null;
		}
		if (abstractType.isNull()) {// null类型不能比任何类型抽象
			return null;
		}
		if (type.isNull()) {// 任何类型都能比null抽象
			return 0;
		}
		if (type.equals(abstractType)) {// 这个方法还要判断泛型
			return 0;
		}
		Integer score = getAbstractScore(abstractType, linker.getSuperType(type.toBox()));// 这个方法中，还要考虑到自动拆组包
		if (score != null) {
			return score - 1;
		}
		for (IType interfaceType : linker.getInterfaceTypes(type)) {// 接口
			Integer score1 = getAbstractScore(abstractType, interfaceType);
			if (score1 != null) {
				return score1 - 1;
			}
		}
		return null;
	}

	public boolean isMoreAbstract(IType abstractType, IType type) {
		return getAbstractScore(abstractType, type) != null;
	}

}

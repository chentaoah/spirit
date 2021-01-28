package com.sum.spirit.core.visiter.action.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.core.common.enums.ModifierEnum;
import com.sum.spirit.core.common.enums.TypeEnum;
import com.sum.spirit.core.visiter.pojo.IType;
import com.sum.spirit.utils.SpringUtils;

public abstract class AbstractAdaptiveLinker implements ClassLinker, InitializingBean {

	public List<ClassLinker> linkers;

	@Override
	public void afterPropertiesSet() throws Exception {
		linkers = SpringUtils.getBeansAndSort(ClassLinker.class, getClass());// 排除自己
	}

	public ClassLinker getLinker(IType type) {
		for (ClassLinker linker : linkers) {
			if (linker.canLink(type)) {
				return linker;
			}
		}
		return null;
	}

	@Override
	public boolean canLink(IType type) {
		return getLinker(type) != null;
	}

	@Override
	public <T> T toClass(IType type) {
		return getLinker(type).toClass(type);
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return getLinker(type).getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		if (type.isPrimitive()) {
			return null;
		}
		if (type.isArray()) {
			return TypeEnum.Object.value;
		}
		IType superType = getLinker(type).getSuperType(type);
		if (superType == null) {
			return null;
		}
		int modifiers = type.getModifiers();
		if (modifiers == ModifierEnum.THIS.value || modifiers == ModifierEnum.SUPER.value) {
			superType.setModifiers(ModifierEnum.SUPER.value);

		} else if (modifiers == ModifierEnum.PUBLIC.value) {
			superType.setModifiers(ModifierEnum.PUBLIC.value);
		}
		return superType;
	}

	@Override
	public List<IType> getInterfaceTypes(IType type) {
		if (type.isPrimitive()) {
			return new ArrayList<>();
		}
		if (type.isArray()) {
			return new ArrayList<>();
		}
		return getLinker(type).getInterfaceTypes(type);
	}

	@Override
	public boolean isMoreAbstract(IType abstractType, IType type) {
		if (type == null) {
			return false;
		}
		// null类型不能比任何类型抽象
		if (abstractType.isNull()) {
			return false;
		}
		// 任何类型都能比null抽象
		if (type.isNull()) {
			return true;
		}
		// 这个方法还要判断泛型
		if (type.equals(abstractType)) {
			return true;
		}
		// 这个方法中，还要考虑到自动拆组包
		if (isMoreAbstract(abstractType, getSuperType(type.getWrappedType()))) {
			return true;
		}
		// 接口
		for (IType inter : getInterfaceTypes(type)) {
			if (isMoreAbstract(abstractType, inter)) {
				return true;
			}
		}
		return false;
	}

}

package com.sum.spirit.core.compile.linker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.common.utils.Lists;
import com.sum.spirit.common.utils.SpringUtils;
import com.sum.spirit.core.api.ClassLinker;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.entity.StaticTypes;

public abstract class AbstractAdaptiveLinker implements ClassLinker, InitializingBean {

	public List<ClassLinker> linkers;

	@Override
	public void afterPropertiesSet() throws Exception {
		linkers = SpringUtils.getBeansAndSort(ClassLinker.class, getClass());// 排除自己
	}

	public ClassLinker getLinker(IType type) {
		return Lists.findOne(linkers, linker -> linker.isHandle(type));
	}

	@Override
	public boolean isHandle(IType type) {
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
			return StaticTypes.OBJECT;
		}
		// 如果不存在父类，则返回Object
		IType superType = getLinker(type).getSuperType(type);
		if (superType == null) {
			if (!StaticTypes.OBJECT.equals(type)) {
				return StaticTypes.OBJECT;
			} else {
				return null;
			}
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

}

package com.sum.spirit.core.type;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.sum.spirit.api.ClassLinker;
import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.pojo.enums.ModifierEnum;
import com.sum.spirit.pojo.enums.TypeEnum;

public abstract class AbsAdaptiveLinker implements ClassLinker {

	@Autowired
	@Qualifier("codeLinker")
	public ClassLinker codeLinker;
	@Autowired
	@Qualifier("nativeLinker")
	public ClassLinker nativeLinker;

	@Override
	public <T> T toClass(IType type) {
		return !type.isNative() ? codeLinker.toClass(type) : nativeLinker.toClass(type);
	}

	@Override
	public int getTypeVariableIndex(IType type, String genericName) {
		return !type.isNative() ? codeLinker.getTypeVariableIndex(type, genericName) : nativeLinker.getTypeVariableIndex(type, genericName);
	}

	@Override
	public IType getSuperType(IType type) {
		if (type.isPrimitive()) {
			return null;
		}
		if (type.isArray()) {
			return TypeEnum.Object.value;
		}
		IType superType = !type.isNative() ? codeLinker.getSuperType(type) : nativeLinker.getSuperType(type);
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
		return !type.isNative() ? codeLinker.getInterfaceTypes(type) : nativeLinker.getInterfaceTypes(type);
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

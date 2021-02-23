package com.sum.spirit.core.compile.deduce;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sum.spirit.common.enums.KeywordEnum;
import com.sum.spirit.common.enums.ModifierEnum;
import com.sum.spirit.core.clazz.entity.IClass;
import com.sum.spirit.core.clazz.entity.IType;
import com.sum.spirit.core.compile.entity.StaticTypes;
import com.sum.spirit.core.compile.linker.TypeFactory;
import com.sum.spirit.core.element.entity.Token;

@Component
public class TypeDerivator {

	@Autowired
	public TypeFactory factory;

	public IType getTargetType(IType type) {
		return factory.create(type.getTargetName());
	}

	public IType getBoxType(IType type) {
		IType boxType = StaticTypes.getBoxType(type.getClassName());
		return boxType != null ? boxType : type;
	}

	public IType toSuper(IType type) {
		type.setModifiers(ModifierEnum.SUPER.value);
		return type;
	}

	public IType toThis(IType type) {
		type.setModifiers(ModifierEnum.THIS.value);
		return type;
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

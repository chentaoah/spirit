package com.sum.spirit.core.clazz.frame;

import com.sum.spirit.core.clazz.entity.IType;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public abstract class TypeEntity extends ContextEntity {
	@NonNull
	private IType type;
}

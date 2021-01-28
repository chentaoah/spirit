package com.sum.spirit.core.clazz.pojo.api;

import com.sum.spirit.core.visiter.pojo.IType;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public abstract class TypeUnit {
	@NonNull
	private IType type;
}

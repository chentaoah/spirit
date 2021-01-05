package com.sum.spirit.pojo.clazz.api;

import com.sum.spirit.pojo.common.IType;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public abstract class TypeUnit {
	@NonNull
	private IType type;
}

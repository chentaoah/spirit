package com.sum.spirit.pojo.enums;

import java.lang.reflect.Modifier;

public enum ModifierEnum {

	THIS(Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE),

	SUPER(Modifier.PUBLIC | Modifier.PROTECTED),

	PUBLIC(Modifier.PUBLIC);

	public int value;

	private ModifierEnum(int value) {
		this.value = value;
	}
}

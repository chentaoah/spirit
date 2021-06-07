package com.gitee.spirit.common.enums;

import java.lang.reflect.Modifier;

public enum AccessLevelEnum {

	PRIVATE(Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE), //
	PROTECTED(Modifier.PUBLIC | Modifier.PROTECTED), //
	PUBLIC(Modifier.PUBLIC);//

	public int value;

	private AccessLevelEnum(int value) {
		this.value = value;
	}
}

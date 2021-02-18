package com.sum.spirit.core.clazz.entity;

import com.sum.spirit.common.entity.Token;
import com.sum.spirit.core.clazz.frame.TokenUnit;

public class IVariable extends TokenUnit {

	public String blockId;

	public IVariable(Token token) {
		super(token);
	}

	@Override
	public String getName() {
		if (token == null) {
			return "NO_NAME";
		}
		return super.getName();
	}

}

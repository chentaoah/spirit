package com.sum.spirit.core.clazz.pojo;

import com.sum.spirit.core.clazz.frame.TokenUnit;
import com.sum.spirit.core.element.pojo.Token;

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

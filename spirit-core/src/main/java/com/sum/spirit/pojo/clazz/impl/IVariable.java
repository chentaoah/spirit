package com.sum.spirit.pojo.clazz.impl;

import com.sum.spirit.pojo.clazz.Tokened;
import com.sum.spirit.pojo.element.impl.Token;

public class IVariable extends Tokened {

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
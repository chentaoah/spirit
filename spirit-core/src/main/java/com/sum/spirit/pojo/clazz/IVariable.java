package com.sum.spirit.pojo.clazz;

import com.sum.spirit.pojo.clazz.api.Tokened;
import com.sum.spirit.pojo.element.Token;

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

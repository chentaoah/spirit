package com.sum.spirit.pojo.clazz;

import com.sum.spirit.lib.Assert;

public abstract class Typed {

	private IType type;

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		Assert.notNull(type, "Type cannot be null!");
		this.type = type;
	}

}

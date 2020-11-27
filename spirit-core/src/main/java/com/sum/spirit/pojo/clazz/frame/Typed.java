package com.sum.spirit.pojo.clazz.frame;

import com.sum.spirit.pojo.common.IType;
import com.sum.spirit.utils.Assert;

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

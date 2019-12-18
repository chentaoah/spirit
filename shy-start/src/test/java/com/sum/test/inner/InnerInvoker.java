package com.sum.test.inner;

import com.sum.test.inner.Outside.Inner;

public class InnerInvoker {

	public Inner inner = new Inner();

	public int testInner() {
		return inner.getAge();
	}

}

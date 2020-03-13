package com.sum.test.inner;

import com.sum.test.inner.Inner;

public class InnerInvoker {

	public Inner inner = new Inner();

	public int testInner() {
		return inner.getAge();
	}

}

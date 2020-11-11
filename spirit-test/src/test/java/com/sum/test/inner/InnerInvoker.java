package com.sum.test.inner;

public class InnerInvoker {

	public Inner inner = new Inner();

	public int testInner() {
		return inner.getAge();
	}

}

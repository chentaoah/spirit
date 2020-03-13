package com.sum.test.inner;

import com.sum.test.inner.Inner;

class Outside {

	public int f1 = 100;

	public double f2 = 100.01;

	public String getName() {
		return "I am Outside";
	}

	public int getF1() {
		return f1;
	}

	public int testInner() {
		Inner inner = new Inner();
		return inner.getAge();
	}

}

package com.sum.test.deduce;

import com.sum.test.deduce.Father;

public class Child extends Father {

	public String sayHelloLikeFather() {
		return this.sayHello();
	}

}

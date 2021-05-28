package com.gitee.spirit.example.deduce;

public class Father {

	public Child child = new Child();
	public String name = "caixukun";

	public String sayHello() {
		return "hello";
	}

	public Child getChild() {
		return child;
	}

}

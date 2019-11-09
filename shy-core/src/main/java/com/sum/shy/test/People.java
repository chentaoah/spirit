package com.sum.shy.test;

import java.util.List;

public class People {

	public Child child;

	public List<String> list;

	public static People getInstance() {
		return new People();
	}

	public void say(int x, int y) {
		System.out.println(x + y);
	}

	public List<String> getName() {
		return null;
	}
}

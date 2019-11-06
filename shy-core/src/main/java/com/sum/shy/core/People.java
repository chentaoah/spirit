package com.sum.shy.core;

public class People {

	public static People getInstance() {
		return new People();
	}

	public void say(int x, int y) {
		System.out.println(x + y);
	}
}

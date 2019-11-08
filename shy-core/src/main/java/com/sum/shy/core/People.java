package com.sum.shy.core;

import java.util.List;

public class People {
	
	public Child child;

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

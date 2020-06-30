package com.sum.test.type;

import java.util.HashMap;

@SuppressWarnings("serial")
public class GenericType<T, K> extends HashMap<T, Integer> {

	public Integer testGeneric() {
		return get("test");
	}

	public HashMap<T, K> test() {
		return new HashMap<T, K>();
	}

	public Integer test1() {
		GenericType<String, String> g = new GenericType<String, String>();
		return g.get("test");
	}

	public HashMap<String, String> test2() {
		GenericType<String, String> g = new GenericType<String, String>();
		return g.test();
	}

	public void testB() {
	}

}

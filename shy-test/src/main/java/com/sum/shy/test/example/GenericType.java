package com.sum.shy.test.example;

import java.util.HashMap;
import java.util.Map;

public class GenericType<T, K> {

	public String test(T t) {
		return "";
	}

//	public int test(K k) {
//		return "";
//	}

	public void Test(String string) {
		Map<String, Object> map = new HashMap<>();
//		map.put(123, null);
		map.put("", 123);
	}

}

package com.sum.shy.test;

import java.util.HashMap;
import java.util.Map;

public class Test {

	public static void main(String[] args) {
		int a = 100;
		int b = 100;
		Map<String, Boolean> map = new HashMap<>();
		map.put("key", a == b);

	}

}
